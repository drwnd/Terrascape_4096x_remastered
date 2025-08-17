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
        state.computeNextGameTickVelocity(direction, lastPosition, velocity);
        state = state.next;
        return position;
    }

    public Vector3f getVelocity() {
        return new Vector3f(velocity);
    }

    public static void normalizeToMaxComponent(Vector3f velocity) {
        float max = Math.abs(velocity.get(velocity.maxComponent()));
        if (max < 1E-4) return;
        velocity.normalize(max);
    }

    public void registerKey(int key, int action) {
        state.registerKeyInput(key, action);
    }

    private MovementState state = new FlyingState();
    private final Vector3f velocity;
}
