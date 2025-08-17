package player.movement;

import org.joml.Vector3f;
import player.Position;

public abstract class MovementState {

    MovementState next = this;

    /**
     * Computes the velocity the player should have in the next Gametick.
     *
     * @param playerDirection The direction the player (not necessarily the camera) is facing
     * @param lastPositon The position of the player in the last Gametick
     * @param inOutVelocity The velocity of the player in the last Gametick.
     *                      WHATEVER VALUE THIS HAS AFTER THE METHOD RETURNS IS THE VELOCITY OF THE PLAYER IN THE NEXT GAME TICK!
     */
    abstract void computeNextGameTickVelocity(Vector3f playerDirection, Position lastPositon, Vector3f inOutVelocity);

    abstract void registerKeyInput(int key, int action);
}
