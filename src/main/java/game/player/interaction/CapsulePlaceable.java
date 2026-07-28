package game.player.interaction;

import core.settings.stand_alones.StandAloneIntSetting;
import core.utils.Saver;
import core.utils.Vector3l;

import game.server.Chunk;
import game.server.generation.Structure;
import game.utils.Utils;

import org.joml.primitives.AABBi;
import org.joml.primitives.Intersectionf;

import java.util.ArrayList;

import static game.utils.Constants.*;

public final class CapsulePlaceable implements Placeable {

    public CapsulePlaceable(byte material, int radius) {
        this.material = material;
        this.radius.setValue(radius);
    }

    public static CapsulePlaceable load(Saver<?> saver) {
        return new CapsulePlaceable(saver.loadByte(), saver.loadInt());
    }


    public void setStartEndPositions(Vector3l startPosition, Vector3l endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        minPosition = maxPosition = null;
    }

    @Override
    public void place(Vector3l position, int lod) {
        if (minPosition == null || maxPosition == null) return;
    }

    @Override
    public ArrayList<Chunk> getAffectedChunks() {
        return affectedChunks;
    }

    @Override
    public Structure getStructure() {
        return new Structure(STONE);
    }

    @Override
    public boolean intersectsAABB(Vector3l position, Vector3l min, Vector3l max) {
        if (minPosition == null || maxPosition == null) return true;

        int minX = (int) (minPosition.x - min.x), maxX = (int) (maxPosition.x - min.x);
        int minY = (int) (minPosition.y - min.y), maxY = (int) (maxPosition.y - min.y);
        int minZ = (int) (minPosition.z - min.z), maxZ = (int) (maxPosition.z - min.z);

        AABBi aabb = new AABBi(0, 0, 0, (int) (max.x - min.x), (int) (max.y - min.y), (int) (max.z - min.z));
        return aabb.intersectLineSegment(minX, minY, minZ, maxX, maxY, maxZ, null) != Intersectionf.OUTSIDE;
    }

    @Override
    public void offsetPosition(Vector3l position, int targetedSide) {
        if (startPosition == null || endPosition == null) return;
        RepeatPlaceable.offsetPositions(startPosition, endPosition, targetedSide, null);
        minPosition = new Vector3l(Utils.min(startPosition, endPosition));
        maxPosition = new Vector3l(Utils.max(startPosition, endPosition));
    }

    @Override
    public void spawnParticles(Vector3l position) {
        if (minPosition == null || maxPosition == null) return;
        // TODO
    }

    @Override
    public void save(Saver<?> saver) {
        saver.saveByte((byte) 20);
        saver.saveByte(material);
        saver.saveInt(radius.value());
    }

    private final byte material;
    private final ArrayList<Chunk> affectedChunks = new ArrayList<>();
    private final StandAloneIntSetting radius = new StandAloneIntSetting(1, 64, 8);

    private Vector3l startPosition, endPosition;
    private Vector3l minPosition, maxPosition;
}
