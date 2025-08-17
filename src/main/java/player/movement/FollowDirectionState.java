package player.movement;

import org.joml.Vector3f;
import player.Position;

public final class FollowDirectionState extends MovementState {
    @Override
    protected MovementState computeNextGameTickVelocity(Vector3f playerDirection, Position lastPositon, Vector3f inOutVelocity) {
        inOutVelocity.set(playerDirection);
        return this;
    }
}
