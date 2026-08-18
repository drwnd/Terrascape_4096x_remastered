package game.player.movement;

import core.rendering_api.Input;
import core.utils.MathUtils;

import game.settings.KeySettings;
import game.settings.ToggleSettings;
import game.utils.Position;

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
    public byte getIdentifier() {
        return 3;
    }

    @SuppressWarnings("unused")
    private float verticalFlySpeed, horizontalFlySpeed, sneakSpeedModifier, sprintSpeedModifier, flyFastSpeedModifier;
}
