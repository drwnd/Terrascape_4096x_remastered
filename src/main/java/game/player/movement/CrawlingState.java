package game.player.movement;

import core.rendering_api.Input;
import core.utils.MathUtils;

import game.assets.Model;
import game.player.rendering.Camera;
import game.server.Game;
import game.settings.KeySettings;
import game.settings.OptionSettings;
import game.utils.Position;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static game.assets.Models.*;
import static game.assets.Models.LEFT_LEG;
import static game.assets.Models.RIGHT_ARM;
import static game.assets.Models.RIGHT_LEG;
import static org.lwjgl.glfw.GLFW.*;

public final class CrawlingState extends MovementState {

    @Override
    Vector3f computeNextGameTickAcceleration(Vector3f playerRotation, Position lastPosition) {
        if (!Input.isKeyPressed(KeySettings.CRAWL)) next = MovementState.load(SneakingState.class);
        if (Input.isKeyPressed(KeySettings.SPRINT) && Input.isKeyPressed(KeySettings.MOVE_FORWARD) && intersectsLiquid(lastPosition, this))
            next = MovementState.load(SwimmingState.class);

        Vector3f velocityChange = new Vector3f();
        Vector3f playerDirection = MathUtils.getHorizontalDirection(playerRotation);
        float speed = getMovementSpeed(lastPosition, crawlingSpeed, inAirSpeed, swimStrength);

        applyXZMovement(velocityChange, speed, 1.0F);

        if (Input.isKeyPressed(KeySettings.JUMP)) handleJump(lastPosition, velocityChange, jumpStrength, swimStrength);

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
    public double applyAnimation(Model playerCharacter, Camera camera, double animationTimer, float frameTime) {
        Matrix4f[] transforms = playerCharacter.transforms();
        Model.ModelBox[] boxes = playerCharacter.boxes();
        float fraction = Math.clamp(Game.getServer().getCurrentGameTickFraction(), 0, 1);
        Vector3f velocity = movement.getRenderVelocity().mul(fraction).add(movement.getOldRenderVelocity().mul(1 - fraction));
        Vector3f cameraRotation = camera.getRotation();

        Vector3f direction = MathUtils.getHorizontalDirection(cameraRotation);
        float angle = (float) -Math.toRadians(cameraRotation.y);
        float sidewaysVelocity = -velocity.x * direction.z + velocity.z * direction.x;
        float sidewaysTilt = (float) Math.clamp(sidewaysVelocity * 0.2F, -Math.PI * 0.25, Math.PI * 0.25);
        boolean shiftCharacter = OptionSettings.PERSPECTIVE.value() == Camera.Perspective.FIRST_PERSON;

        for (int index = BODY; index < transforms.length; index++)
            transforms[index].identity()
                    .rotate(angle - sidewaysTilt, 0, 1, 0)
                    .translate(boxes[index].position())
                    .translate(0, 0, shiftCharacter ? 3 : 0);
        if (shiftCharacter) transforms[HEAD].zero();
        else transforms[HEAD].identity()
                .rotate(angle, 0, 1, 0)
                .translate(boxes[HEAD].position())
                .rotate(-sidewaysTilt, 0, 1, 0)
                .translate(0, 0, -6)
                .rotate(sidewaysTilt, 0, 1, 0)
                .translate(0, 0, 6);

        float rotationSpeed = camera.getCurrentRotationSpeed(fraction).length() * 0.05F;
        float speed = (float) Math.clamp(velocity.length() * 0.2 + rotationSpeed, -Math.PI * 0.5, Math.PI * 0.5);

        transforms[HEAD].translate(0, -22, -6).rotate((float) -Math.PI * 0.25F, 1, 0, 0);
        transforms[BODY].translate(0, -10, 6).rotate((float) -Math.PI * 0.5F, 1, 0, 0);
        transforms[LEFT_ARM].translate(0, -20, -4).rotate((float) -Math.PI * 0.5F, 1, 0, 0);
        transforms[RIGHT_ARM].translate(0, -20, -4).rotate((float) -Math.PI * 0.5F, 1, 0, 0);
        transforms[LEFT_LEG].translate(0, -10, 6).rotate((float) -Math.PI * 0.5F, 1, 0, 0);
        transforms[RIGHT_LEG].translate(0, -10, 6).rotate((float) -Math.PI * 0.5F, 1, 0, 0);

        return animationTimer + frameTime * 0.005;
    }

    @Override
    public byte getIdentifier() {
        return 4;
    }

    @SuppressWarnings("unused")
    private float jumpStrength, swimStrength, crawlingSpeed, inAirSpeed;
}
