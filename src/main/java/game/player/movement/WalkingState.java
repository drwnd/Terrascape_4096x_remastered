package game.player.movement;

import core.rendering_api.Input;
import core.sound.Sound;
import core.utils.MathUtils;

import game.assets.Model;
import game.server.material.Material;
import game.settings.FloatSettings;
import game.settings.KeySettings;
import game.utils.Position;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public final class WalkingState extends MovementState {

    @Override
    Vector3f computeNextGameTickAcceleration(Vector3f playerRotation, Position lastPosition) {
        if (Input.isKeyPressed(KeySettings.SNEAK)) next = MovementState.load(SneakingState.class);
        if (Input.isKeyPressed(KeySettings.CRAWL)) next = MovementState.load(CrawlingState.class);
        if (Input.isKeyPressed(KeySettings.SPRINT) && Input.isKeyPressed(KeySettings.MOVE_FORWARD) && intersectsLiquid(lastPosition, this))
            next = MovementState.load(SneakingState.class);

        Vector3f velocityChange = new Vector3f();
        Vector3f playerDirection = MathUtils.getHorizontalDirection(playerRotation);
        float speed = getMovementSpeed(lastPosition, walkingSpeed, inAirSpeed, swimStrength);

        applyXZMovement(velocityChange, speed, sprintSpeedModifier);

        if (Input.isKeyPressed(KeySettings.JUMP)) {
            playJumpSound(lastPosition);
            handleJump(lastPosition, velocityChange, jumpStrength, swimStrength);
            if (Input.isKeyPressed(KeySettings.MOVE_FORWARD) && Input.isKeyPressed(KeySettings.SPRINT) && movement.isWideGrounded())
                velocityChange.x += jumpSpeedGain;
        }

        normalizeXZToMaxComponent(velocityChange);
        toWorldDirection(velocityChange, playerDirection);

        return velocityChange;
    }

    @Override
    void handleInput(int key, int action) {
        if (key == KeySettings.JUMP.keybind() && action == GLFW_PRESS) {
            if (System.nanoTime() - lastJumpTime < JUMP_FLYING_INTERVALL) next = MovementState.load(FlyingState.class);
            lastJumpTime = System.nanoTime();
        }
    }

    @Override
    public byte getIdentifier() {
        return 0;
    }

    @Override
    public int ticksBetweenFootsteps() {
        return Input.isKeyPressed(KeySettings.SPRINT) ? ticksBetweenFootstepsWhenSprinting : super.ticksBetweenFootsteps();
    }

    @Override
    public void applyAnimation(Model playerCharacter, Vector3f cameraRotation) {
        super.applyAnimation(playerCharacter, cameraRotation);
        Matrix4f[] transforms = playerCharacter.transforms();

        float speed = (float) Math.clamp(movement.getRenderVelocity().length() * 0.2, -Math.PI * 0.5, Math.PI * 0.5);
        double time = System.currentTimeMillis() * 0.01;
        transforms[0].rotate((float) -Math.toRadians(cameraRotation.x), 1.0F, 0.0F, 0.0F);
        transforms[2].rotate((float) Math.sin(time) * speed, 1.0F, 0.0F, 0.0F);
        transforms[3].rotate((float) -Math.sin(time) * speed, 1.0F, 0.0F, 0.0F);
        transforms[4].rotate((float) -Math.sin(time) * speed * 0.5F, 1.0F, 0.0F, 0.0F);
        transforms[5].rotate((float) Math.sin(time) * speed * 0.5F, 1.0F, 0.0F, 0.0F);
    }

    private void playJumpSound(Position position) {
        if (!movement.isWideGrounded()) return;
        byte standingMaterial = getStandingMaterial(position);
        Sound.play3D(Material.getJumpSounds(standingMaterial), FloatSettings.JUMP_AUDIO, position, null);
    }

    @SuppressWarnings("unused")
    private float jumpStrength, swimStrength, walkingSpeed, inAirSpeed, sprintSpeedModifier, jumpSpeedGain;
    @SuppressWarnings("unused")
    private int ticksBetweenFootstepsWhenSprinting;
}
