package game.player.movement;

import core.sound.Sound;

import game.server.Game;
import game.server.World;
import game.server.material.Material;
import game.server.material.Properties;
import game.settings.FloatSettings;
import game.settings.ToggleSettings;
import game.utils.Position;

import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import static game.utils.Constants.*;

public final class Movement {

    public Movement() {
        state = MovementState.load(WalkingState.class);
        state.movement = this;
    }

    public Position computeNextGameTickPosition(Position lastPosition, Vector3f rotation) {
        Position position = new Position(lastPosition);
        grounded = velocity.y == 0.0F && checkGrounded(position);

        Vector3f acceleration = Game.getPlayer().canDoActiveActions() ? state.computeNextGameTickAcceleration(rotation, position) : new Vector3f(0.0F);
        state.changeVelocity(velocity, acceleration, position, rotation);

        velocity.set(move(position));
        oldRenderVelocity = renderVelocity;
        renderVelocity = position.vectorFrom(lastPosition);
        playFootstepSound(position);

        if (ToggleSettings.NO_CLIP.value() || noCollision(position, state.next)) state = state.next;
        state.movement = this;
        return position;
    }

    public void handleInput(int button, int action) {
        state.handleInput(button, action);
    }

    public Vector3f getVelocity() {
        return new Vector3f(velocity);
    }

    public Vector3f getRenderVelocity() {
        return new Vector3f(renderVelocity);
    }

    public Vector3f getOldRenderVelocity() {
        return new Vector3f(oldRenderVelocity);
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity.set(velocity);
    }

    public void setState(byte identifier) {
        MovementState state = MovementState.getStateFromIdentifier(identifier);
        if (state == null) return;
        this.state = state;
        this.state.movement = this;
    }

    public void setState(MovementState state) {
        if (state == null) return;
        this.state = state;
        this.state.movement = this;
    }

    public MovementState getState() {
        return state;
    }

    public boolean isGrounded() {
        return grounded;
    }


    private boolean checkGrounded(Position position) {
        Vector3i hitboxSize = state.getHitboxSize();

        long minX = position.longX - (hitboxSize.x >> 1);
        long minZ = position.longZ - (hitboxSize.z >> 1);
        long maxX = position.longX + (hitboxSize.x >> 1);
        long maxZ = position.longZ + (hitboxSize.z >> 1);
        return containsCollidableVoxel(position.longY - 1, minX, minZ, maxX, maxZ);
    }

    private Vector3f move(Position position) {
        if (ToggleSettings.NO_CLIP.value()) {
            position.add(velocity.x, velocity.y, velocity.z);
            return velocity;
        }

        Vector3f nextGTVelocity = new Vector3f(velocity);
        Vector3f toMoveDistance = new Vector3f(this.velocity);
        Vector3d units = new Vector3d();
        Vector3d lengths = new Vector3d();
        computeRayCastConstants(toMoveDistance, units, lengths);
        while (toMoveDistance.lengthSquared() != 0) {
            int minComponent = lengths.minComponent();
            move(nextGTVelocity, toMoveDistance, position, units, lengths, minComponent);
        }
        return nextGTVelocity;
    }

    private void move(Vector3f nextVelocity, Vector3f toMoveDistance, Position position, Vector3d units, Vector3d lengths, int component) {

        float toMove = toMoveDistance.get(component), moved;
        if (toMove == 0) {
            advanceLength(units, lengths, component);
            return;
        }
        if (Math.abs(toMove) <= 1) {
            moved = toMove;
            position.addComponent(component, toMove);
            toMoveDistance.setComponent(component, 0);
        } else {
            int directionComponent = toMove > 0 ? 1 : -1;
            moved = directionComponent;
            position.addComponent(component, directionComponent);
            toMoveDistance.setComponent(component, toMove - directionComponent);
        }
        if (shouldStopAtEdge(position, component)) stopAndUndoMove(nextVelocity, toMoveDistance, position, units, lengths, component, moved);
        if (collides(position, component)) resolveCollision(nextVelocity, toMoveDistance, position, units, lengths, component, moved);
        else advanceLength(units, lengths, component);
        if (toMoveDistance.get(component) == 0) lengths.setComponent(component, Double.POSITIVE_INFINITY);
    }

    private void resolveCollision(Vector3f nextVelocity, Vector3f toMoveDistance, Position position, Vector3d units, Vector3d lengths,
                                  int component, float moved) {
        float requiredStepHeight = getRequiredStepHeight(position, component);
        if (canAutoStep(position, requiredStepHeight)) {
            position.addComponent(Y_COMPONENT, requiredStepHeight);
            advanceLength(units, lengths, component);
            toMoveDistance.y = 0.0F;
            computeRayCastConstants(toMoveDistance, units, lengths);
            return;
        }
        stopAndUndoMove(nextVelocity, toMoveDistance, position, units, lengths, component, moved);
    }

    private static void stopAndUndoMove(Vector3f nextVelocity, Vector3f toMoveDistance, Position position, Vector3d units, Vector3d lengths,
                                        int component, float moved) {
        nextVelocity.setComponent(component, 0);
        lengths.setComponent(component, Double.POSITIVE_INFINITY);
        toMoveDistance.setComponent(component, 0);
        position.addComponent(component, -moved);

        if (component == X_COMPONENT) position.fractionX = moved < 0 ? 0 : 0.9999999F;
        if (component == Y_COMPONENT) position.fractionY = moved < 0 ? 0 : 0.9999999F;
        if (component == Z_COMPONENT) position.fractionZ = moved < 0 ? 0 : 0.9999999F;

        position.add(0, 0, 0);
        computeRayCastConstants(toMoveDistance, units, lengths);
    }

    private float getRequiredStepHeight(Position position, int component) {
        if (component == Y_COMPONENT) return Float.POSITIVE_INFINITY;
        Vector3i hitboxSize = state.getHitboxSize();
        World world = Game.getWorld();

        long startX = getStartX(position, hitboxSize, component);
        long startY = getStartY(position, hitboxSize, component);
        long startZ = getStartZ(position, hitboxSize, component);

        int width = component == X_COMPONENT ? 1 : hitboxSize.x;
        int height = hitboxSize.y;
        int depth = component == Z_COMPONENT ? 1 : hitboxSize.z;

        for (long y = startY + height - 1; y != startY - 1; y--)
            for (long x = startX; x != startX + width; x++)
                for (long z = startZ; z != startZ + depth; z++) {
                    byte material = world.getMaterial(x, y, z, 0);
                    if (Properties.doesntHaveProperties(material, NO_COLLISION)) return (y - position.longY + 1) - position.fractionY;
                }
        return 0.0F;
    }

    private boolean canAutoStep(Position position, float requiredStepHeight) {
        if (requiredStepHeight == 0.0F) return false;

        boolean swimming = MovementState.intersectsLiquid(position, state);
        float maxStepHeight = state.getMaxAutoStepHeight();
        if ((!checkGrounded(position) && !swimming) || requiredStepHeight > maxStepHeight) return false;

        Position steppedPosition = new Position(position);
        steppedPosition.addComponent(Y_COMPONENT, requiredStepHeight);
        return noCollision(steppedPosition, state);
    }

    private boolean collides(Position position, int component) {
        Vector3i hitboxSize = state.getHitboxSize();

        long startX = getStartX(position, hitboxSize, component);
        long startY = getStartY(position, hitboxSize, component);
        long startZ = getStartZ(position, hitboxSize, component);

        int width = component == X_COMPONENT ? 1 : hitboxSize.x;
        int height = component == Y_COMPONENT ? 1 : hitboxSize.y;
        int depth = component == Z_COMPONENT ? 1 : hitboxSize.z;

        return collides(startX, startY, startZ, width, height, depth);
    }

    private boolean shouldStopAtEdge(Position position, int component) {
        if (component == Y_COMPONENT || velocity.y > 0.0F || !state.preventsFallingFromEdge()) return false;
        Position testPosition = new Position(position).addComponent(Y_COMPONENT, -state.getMaxAutoStepHeight() - 1);
        return noCollision(testPosition, state);
    }

    private long getStartX(Position position, Vector3i hitboxSize, int component) {
        return component == X_COMPONENT && velocity.x > 0 ? position.longX + (hitboxSize.x >> 1) - 1 : position.longX - (hitboxSize.x >> 1);
    }

    private long getStartY(Position position, Vector3i hitboxSize, int component) {
        return component == Y_COMPONENT && velocity.y > 0 ? position.longY + hitboxSize.y - 1 : position.longY;
    }

    private long getStartZ(Position position, Vector3i hitboxSize, int component) {
        return component == Z_COMPONENT && velocity.z > 0 ? position.longZ + (hitboxSize.z >> 1) - 1 : position.longZ - (hitboxSize.z >> 1);
    }

    private void playFootstepSound(Position position) {
        long currentGameTick = Game.getServer().getCurrentGameTick();
        int ticksBetweenFootsteps = state.ticksBetweenFootsteps();
        if (ticksBetweenFootsteps < 0 || currentGameTick < lastFootstepTick + ticksBetweenFootsteps || velocity.lengthSquared() < 1) return;
        lastFootstepTick = currentGameTick;

        byte standingMaterial = state.getStandingMaterial(position);
        Sound.play3D(Material.getStepSounds(standingMaterial), FloatSettings.FOOTSTEPS_AUDIO, position, null);
    }


    private static boolean noCollision(Position position, MovementState state) {
        Vector3i hitboxSize = state.getHitboxSize();
        long startX = position.longX - (hitboxSize.x >> 1);
        long startY = position.longY;
        long startZ = position.longZ - (hitboxSize.z >> 1);

        return !collides(startX, startY, startZ, hitboxSize.x, hitboxSize.y, hitboxSize.z);
    }

    private static boolean containsCollidableVoxel(long y, long minX, long minZ, long maxX, long maxZ) {
        World world = Game.getWorld();

        for (long x = minX; x != maxX; x++)
            for (long z = minZ; z != maxZ; z++) {
                byte material = world.getMaterial(x, y, z, 0);
                if (Properties.doesntHaveProperties(material, NO_COLLISION)) return true;
            }
        return false;
    }

    private static boolean collides(long startX, long startY, long startZ, int width, int height, int depth) {
        World world = Game.getWorld();

        for (long x = startX; x != startX + width; x++)
            for (long y = startY; y != startY + height; y++)
                for (long z = startZ; z != startZ + depth; z++) {
                    byte material = world.getMaterial(x, y, z, 0);
                    if (Properties.doesntHaveProperties(material, NO_COLLISION)) return true;
                }
        return false;
    }

    private static void computeRayCastConstants(Vector3f toMoveDistance, Vector3d units, Vector3d lengths) {
        double dirXSquared = toMoveDistance.x * toMoveDistance.x;
        double dirYSquared = toMoveDistance.y * toMoveDistance.y;
        double dirZSquared = toMoveDistance.z * toMoveDistance.z;
        units.x = Math.sqrt(1 + (dirYSquared + dirZSquared) / dirXSquared);
        units.y = Math.sqrt(1 + (dirXSquared + dirZSquared) / dirYSquared);
        units.z = Math.sqrt(1 + (dirXSquared + dirYSquared) / dirZSquared);

        lengths.zero();
    }

    private static void advanceLength(Vector3d units, Vector3d lengths, int component) {
        lengths.setComponent(component, lengths.get(component) + units.get(component));
    }


    private long lastFootstepTick = 0;
    private MovementState state;
    private boolean grounded;
    private final Vector3f velocity = new Vector3f();
    private Vector3f renderVelocity = new Vector3f(), oldRenderVelocity = new Vector3f();
}
