package player;

import org.joml.Vector3f;
import org.joml.Vector3i;
import utils.Utils;

public record Position(Vector3i intPosition, Vector3f fractionPosition) {

    public Position(Position position) {
        this(new Vector3i(position.intPosition), new Vector3f(position.fractionPosition));
    }

    public void set(Position position) {
        intPosition.set(position.intPosition);
        fractionPosition.set(position.fractionPosition);
    }

    public void add(float x, float y, float z) {
        fractionPosition.add(x, y, z);
        intPosition.add(Utils.floor(fractionPosition.x), Utils.floor(fractionPosition.y), Utils.floor(fractionPosition.z));
        fractionPosition.set(Utils.fraction(fractionPosition.x), Utils.fraction(fractionPosition.y), Utils.fraction(fractionPosition.z));
    }
}
