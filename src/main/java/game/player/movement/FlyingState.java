package game.player.movement;

import core.rendering_api.Input;
import core.utils.MathUtils;

import game.assets.Model;
import game.player.rendering.Camera;
import game.server.Game;
import game.settings.KeySettings;
import game.settings.ToggleSettings;
import game.utils.Position;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public final class FlyingState extends MovementState {

    @Override
    protected Vector3f computeNextGameTickAcceleration(Vector3f playerRotation, Position lastPosition) {

        Vector3f velocityChange = new Vector3f();
        Vector3f playerDirection = MathUtils.getHorizontalDirection(playerRotation);

        WalkingState.applyXZMovement(velocityChange, horizontalFlySpeed, sprintSpeedModifier);

        if (Input.isKeyPressed(KeySettings.SNEAK)) velocityChange.mul(sneakSpeedModifier);

        if (Input.isKeyPressed(KeySettings.JUMP)) velocityChange.y += verticalFlySpeed;
        if (Input.isKeyPressed(KeySettings.SNEAK)) velocityChange.y -= verticalFlySpeed;

        if (Input.isKeyPressed(KeySettings.FLY_FAST)) velocityChange.mul(flyFastSpeedModifier);
        normalizeToMaxComponent(velocityChange);
        toWorldDirection(velocityChange, playerDirection);

        return velocityChange;
    }

    @Override
    void changeVelocity(Vector3f velocity, Vector3f acceleration, Position playerPosition, Vector3f playerRotation) {
        velocity.add(acceleration).mul(AIR_DRAG);
        if (movement.isThinGrounded() && !ToggleSettings.NO_CLIP.value()) next = MovementState.load(WalkingState.class);
    }

    @Override
    protected void handleInput(int key, int action) {
        if (key == KeySettings.JUMP.keybind() && action == GLFW_PRESS) {
            if (System.nanoTime() - lastJumpTime < JUMP_FLYING_INTERVALL) next = MovementState.load(WalkingState.class);
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

        WalkingState.rotateBody(transforms, boxes, cameraRotation, velocity, direction);

        float rotationSpeed = camera.getCurrentRotationSpeed(fraction).length() * 0.05F;
        float speed = (float) Math.clamp(velocity.length() * 0.2 + rotationSpeed, -Math.PI * 0.5, Math.PI * 0.5);
        transforms[0].rotate((float) -Math.toRadians(cameraRotation.x), 1, 0, 0);
        transforms[2].rotate((float) Math.sin(animationTimer) * speed, 1, 0, 0).rotate(-speed * 0.15F, 0, 0, 1);
        transforms[3].rotate((float) -Math.sin(animationTimer) * speed, 1, 0, 0).rotate(speed * 0.15F, 0, 0, 1);
        transforms[4].rotate((float) -Math.sin(animationTimer) * speed * 0.5F, 1, 0, 0).rotate(-speed * 0.05F, 0, 0, 1);
        transforms[5].rotate((float) Math.sin(animationTimer) * speed * 0.5F, 1, 0, 0).rotate(speed * 0.05F, 0, 0, 1);

        return animationTimer + (Input.isKeyPressed(KeySettings.SPRINT) ? 1.5 : 1) * (Input.isKeyPressed(KeySettings.FLY_FAST) ? 2 : 1) * frameTime * 0.01;
    }

    @Override
    public byte getIdentifier() {
        return 3;
    }

    @SuppressWarnings("unused")
    private float verticalFlySpeed, horizontalFlySpeed, sneakSpeedModifier, sprintSpeedModifier, flyFastSpeedModifier;
}
