package game.player.movement;

import core.rendering_api.Input;
import core.utils.MathUtils;

import game.settings.KeySettings;
import game.utils.Position;

import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public final class SneakingState extends MovementState {

    @Override
    Vector3f computeNextGameTickAcceleration(Vector3f playerRotation, Position lastPosition) {
        if (!Input.isKeyPressed(KeySettings.SNEAK)) next = MovementState.load(WalkingState.class);
        if (Input.isKeyPressed(KeySettings.CRAWL)) next = MovementState.load(CrawlingState.class);
        if (Input.isKeyPressed(KeySettings.SPRINT) && Input.isKeyPressed(KeySettings.MOVE_FORWARD) && intersectsLiquid(lastPosition, this))
            next = MovementState.load(CrawlingState.class);

        Vector3f velocityChange = new Vector3f();
        Vector3f playerDirection = MathUtils.getHorizontalDirection(playerRotation);
        float speed = getMovementSpeed(lastPosition, sneakingSpeed, inAirSpeed, swimStrength);

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
    public byte getIdentifier() {
        return 2;
    }

    @SuppressWarnings("unused")
    private float jumpStrength, swimStrength, sneakingSpeed, inAirSpeed;
}
