package player.movement;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import utils.Position;
import settings.KeySetting;

public final class FollowDirectionState extends MovementState {
    @Override
    protected void computeNextGameTickVelocity(Vector3f playerDirection, Position lastPositon, Vector3f inOutVelocity) {
        inOutVelocity.set(playerDirection);
    }


    @Override
    protected void handleInput(int key, int action) {
        if (key == KeySetting.TOGGLE_FLYING_FOLLOWING_MOVEMENT_STATE.value() && action == GLFW.GLFW_PRESS)
            next = new FlyingState();
    }
}
