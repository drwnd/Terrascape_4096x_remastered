package player.movement;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import utils.Position;
import settings.KeySetting;
import utils.Utils;

public final class FollowDirectionState extends MovementState {
    @Override
    protected void computeNextGameTickVelocity(Vector3f playerRotation, Position lastPositon, Vector3f inOutVelocity) {
        inOutVelocity.set(Utils.getDirection(playerRotation));
    }


    @Override
    protected void handleInput(int key, int action) {
        if (key == KeySetting.TOGGLE_FLYING_FOLLOWING_MOVEMENT_STATE.value() && action == GLFW.GLFW_PRESS)
            next = new FlyingState();
    }
}
