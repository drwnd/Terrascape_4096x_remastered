package player.movement;

import org.joml.Vector3f;
import player.Position;

public final class Movement {

    public Movement(Vector3f initialVelocity) {
        velocity = initialVelocity;
    }

    public Position computeNextGameTickPosition(Position lastPosition, Vector3f direction) {
        Position position = new Position(lastPosition);
        position.add(velocity.x, velocity.y, velocity.z);
        state = state.computeNextGameTickVelocity(direction, lastPosition, velocity);
        return position;
    }

    public Vector3f getVelocity() {
        return new Vector3f(velocity);
    }

    private MovementState state = new FollowDirectionState();
    private final Vector3f velocity;
}
