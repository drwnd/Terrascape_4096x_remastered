package player.movement;

import org.joml.Vector3f;
import utils.Position;

public abstract class MovementState {

    MovementState next = this;

    /**
     * Computes the velocity the player should have in the next Gametick.
     *
     * @param playerRotation The rotation of the player (not necessarily the camera)
     * @param lastPositon The position of the player in the last Gametick
     * @param inOutVelocity The velocity of the player in the last Gametick.
     *                      WHATEVER VALUE THIS HAS AFTER THE METHOD RETURNS IS THE VELOCITY OF THE PLAYER IN THE NEXT GAME TICK!
     */
    abstract void computeNextGameTickVelocity(Vector3f playerRotation, Position lastPositon, Vector3f inOutVelocity);

    abstract void handleInput(int key, int action);
}
