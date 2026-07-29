package game.player.interaction.placeable_shapes;

import core.settings.stand_alones.StandAloneIntSetting;
import core.utils.MathUtils;
import core.utils.Saver;
import core.utils.Vector3l;

import game.language.UiMessages;
import game.player.interaction.PlaceMode;
import game.player.interaction.RepeatPlaceable;
import game.player.interaction.ShapePlaceable;
import game.player.interaction.ShapeSetting;
import game.server.Chunk;
import game.server.Game;
import game.server.generation.Structure;
import game.server.materials_data.MaterialsData;
import game.server.saving.ChunkSaver;
import game.settings.IntSettings;
import game.settings.OptionSettings;
import game.utils.Utils;

import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.primitives.AABBi;
import org.joml.primitives.Intersectionf;

import java.util.ArrayList;
import java.util.Objects;

import static game.utils.Constants.*;

public final class CapsulePlaceable extends ShapePlaceable {

    public CapsulePlaceable(byte material) {
        super(null, material);
        this.material = material;
        loadSettings();
    }

    public static CapsulePlaceable load(Saver<?> saver) {
        CapsulePlaceable placeable = new CapsulePlaceable(saver.loadByte());
        placeable.radius.setValue(saver.loadInt());
        return placeable;
    }

    public void setStartEndPositions(Vector3l startPosition, Vector3l endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public Structure getDisplayStructure() {
        int size = getPreferredSizePowOf2();
        long[] bitMap = new long[size * size * size / 64];
        fillBitMap(bitMap, 0, false);
        return new Structure(Integer.numberOfTrailingZeros(size), material, bitMap);
    }


    @Override
    public void save(Saver<?> saver) {
        saver.saveByte((byte) 20);
        saver.saveByte(material);
        saver.saveInt(radius.value());
    }


    @Override
    protected void fillBitMap(long[] bitMap, int size, boolean forceSize) {
        if (startPosition == null || endPosition == null) return;

        int radius = this.radius.value();
        Vector3l minPosition = Utils.min(startPosition, endPosition).sub(radius, radius, radius);
        Vector3l maxPosition = Utils.max(startPosition, endPosition).add(radius, radius, radius);

        for (long x = minPosition.x; x < maxPosition.x; x++)
            for (long y = minPosition.y; y < maxPosition.y; y++)
                for (long z = minPosition.z; z < maxPosition.z; z++) {
                    if (isOutside(x, y, z)) continue;
                    int index = MaterialsData.getUncompressedIndex((int) (x - minPosition.x), (int) (y - minPosition.y), (int) (z - minPosition.z));
                    bitMap[index >> 6] |= 1L << index;
                }
    }

    @Override
    public Structure getSmallStructure() {
        return new Structure(4, material, CAPSULE_ICON_BITMAP);
    }

    @Override
    public void place(Vector3l position, int lod) {
        if (startPosition == null || endPosition == null) return;

        int radius = this.radius.value();
        Vector3l minPosition = Utils.min(startPosition, endPosition).sub(radius, radius, radius);
        Vector3l maxPosition = Utils.max(startPosition, endPosition).add(radius, radius, radius);

        long chunkStartX = minPosition.x >>> CHUNK_SIZE_BITS + lod;
        long chunkStartY = minPosition.y >>> CHUNK_SIZE_BITS + lod;
        long chunkStartZ = minPosition.z >>> CHUNK_SIZE_BITS + lod;
        long chunkEndX = Utils.getWrappedChunkCoordinate(maxPosition.x >>> CHUNK_SIZE_BITS + lod, chunkStartX, lod);
        long chunkEndY = Utils.getWrappedChunkCoordinate(maxPosition.y >>> CHUNK_SIZE_BITS + lod, chunkStartY, lod);
        long chunkEndZ = Utils.getWrappedChunkCoordinate(maxPosition.z >>> CHUNK_SIZE_BITS + lod, chunkStartZ, lod);
        ChunkSaver saver = new ChunkSaver();

        for (long chunkX = chunkStartX; chunkX <= chunkEndX; chunkX++)
            for (long chunkY = chunkStartY; chunkY <= chunkEndY; chunkY++)
                for (long chunkZ = chunkStartZ; chunkZ <= chunkEndZ; chunkZ++)
                    placeInChunk(saver.loadAndGenerate(chunkX, chunkY, chunkZ, lod));
    }

    @Override
    public ArrayList<Chunk> getAffectedChunks() {
        return affectedChunks;
    }

    @Override
    public Structure getStructure() {
        return getSmallStructure();
    }

    @Override
    public boolean intersectsAABB(Vector3l position, Vector3l min, Vector3l max) {
        if (startPosition == null || endPosition == null) return true;
        return intersectsAABB(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    @Override
    public void offsetPosition(Vector3l position, int targetedSide) {
        if (startPosition == null || endPosition == null) return;
        offsetPositions(startPosition, endPosition, targetedSide);
    }

    public static void offsetPositions(Vector3l startPosition, Vector3l endPosition, int targetedSide) {
        int length = 1 << IntSettings.BREAK_PLACE_ALIGN.value();
        int startMask = -length;

        RepeatPlaceable.offsetPositionFromGround(startPosition, targetedSide, length, length, length);
        startPosition.x &= startMask;
        startPosition.y &= startMask;
        startPosition.z &= startMask;

        endPosition.x -= MathUtils.mod(endPosition.x - startPosition.x, length);
        endPosition.y -= MathUtils.mod(endPosition.y - startPosition.y, length);
        endPosition.z -= MathUtils.mod(endPosition.z - startPosition.z, length);
    }

    @Override
    public void spawnParticles(Vector3l position) {
        if (startPosition == null || endPosition == null) return;
        // TODO
    }

    @Override
    protected ShapePlaceable copyWithMaterialUnique(byte material) {
        CapsulePlaceable copy = new CapsulePlaceable(material);
        copy.radius.setValue(radius.value());
        return copy;
    }

    @Override
    protected ShapeSetting[] getSettings() {
        return new ShapeSetting[]{
                new ShapeSetting(radius, UiMessages.RADIUS, null)
        };
    }

    @Override
    public boolean allowBreak() {
        return Game.getPlayer().getInteractionHandler().getStartTarget() != null;
    }

    @Override
    public boolean allowPlace() {
        return Game.getPlayer().getInteractionHandler().getStartTarget() != null;
    }

    @Override
    public boolean offsetOnBreak() {
        return true;
    }

    @Override
    public int getLengthX() {
        if (startPosition == null || endPosition == null) return 16;
        return (int) Math.abs(startPosition.x - endPosition.x) + 2 * radius.value();
    }

    @Override
    public int getLengthY() {
        if (startPosition == null || endPosition == null) return 16;
        return (int) Math.abs(startPosition.y - endPosition.y) + 2 * radius.value();
    }

    @Override
    public int getLengthZ() {
        if (startPosition == null || endPosition == null) return 16;
        return (int) Math.abs(startPosition.z - endPosition.z) + 2 * radius.value();
    }

    public int getRadius() {
        return radius.value();
    }

    private boolean intersectsAABB(long minX, long minY, long minZ, long maxX, long maxY, long maxZ) {
        int radius = this.radius.value();
        AABBi aabb = new AABBi(-radius, -radius, -radius, (int) (maxX - minX) + radius, (int) (maxY - minY) + radius, (int) (maxZ - minZ) + radius);

        return aabb.intersectLineSegment(
                startPosition.x - minX, startPosition.y - minY, startPosition.z - minZ,
                endPosition.x - minX, endPosition.y - minY, endPosition.z - minZ, new Vector2f()) != Intersectionf.OUTSIDE;
    }

    private void placeInChunk(Chunk chunk) {
        int bits = CHUNK_SIZE_BITS + chunk.LOD;
        Vector3l min = new Vector3l(chunk.X << bits, chunk.Y << bits, chunk.Z << bits);
        Vector3l max = new Vector3l(chunk.X + 1 << bits, chunk.Y + 1 << bits, chunk.Z + 1 << bits);
        if (!intersectsAABB(null, min, max)) return;
        affectedChunks.add(chunk);

        byte[] uncompressedMaterials = new byte[CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE];
        chunk.getMaterials().fillUncompressedMaterialsInto(uncompressedMaterials);
        boolean hasUpdated = false;

        int stepSize = 8 << chunk.LOD;
        for (long totalX = min.x; totalX != max.x; totalX += stepSize)
            for (long totalY = min.y; totalY != max.y; totalY += stepSize)
                for (long totalZ = min.z; totalZ != max.z; totalZ += stepSize)
                    hasUpdated = hasUpdated | placeInSection(uncompressedMaterials, chunk.LOD, totalX, totalY, totalZ);

        if (hasUpdated) {
            chunk.getMaterials().compressIntoData(uncompressedMaterials);
            chunk.setModified();
        }
    }

    private boolean placeInSection(byte[] uncompressedMaterials, int lod, long totalX, long totalY, long totalZ) {
        int sectionSize = 8 << lod;
        if (!intersectsAABB(totalX, totalY, totalZ, totalX + sectionSize, totalY + sectionSize, totalZ + sectionSize)) return false;

        boolean paint = OptionSettings.PLACE_MODE.value() == PlaceMode.PAINT;
        boolean replaceAir = OptionSettings.PLACE_MODE.value() == PlaceMode.REPLACE_AIR;
        boolean breakHeldOnly = OptionSettings.PLACE_MODE.value() == PlaceMode.BREAK_HELD_ONLY;
        byte heldMaterial = breakHeldOnly ? ((ShapePlaceable) Game.getPlayer().getHeldPlaceable()).getMaterial() : AIR;

        int startX = (int) totalX >> lod & CHUNK_SIZE_MASK;
        int startY = (int) totalY >> lod & CHUNK_SIZE_MASK;
        int startZ = (int) totalZ >> lod & CHUNK_SIZE_MASK;

        for (int inChunkX = startX; inChunkX < startX + 8; inChunkX++)
            for (int inChunkY = startY; inChunkY < startY + 8; inChunkY++)
                for (int inChunkZ = startZ; inChunkZ < startZ + 8; inChunkZ++) {

                    long relativeX = totalX + ((long) inChunkX - startX << lod);
                    long relativeY = totalY + ((long) inChunkY - startY << lod);
                    long relativeZ = totalZ + ((long) inChunkZ - startZ << lod);

                    if (isOutside(relativeX, relativeY, relativeZ)) continue;
                    int materialIndex = MaterialsData.getUncompressedIndex(inChunkX, inChunkY, inChunkZ);

                    if (paint && uncompressedMaterials[materialIndex] == AIR
                            || replaceAir && uncompressedMaterials[materialIndex] != AIR
                            || breakHeldOnly && uncompressedMaterials[materialIndex] != heldMaterial) continue;
                    uncompressedMaterials[materialIndex] = material;
                }
        return true;
    }

    private boolean isOutside(long x, long y, long z) {
        // TODO rewrite this without using 6 new-s
        Vector3l a = new Vector3l(startPosition);
        Vector3l b = new Vector3l(endPosition);

        Vector3l pa = new Vector3l(x, y, z).sub(a);
        Vector3l ba = new Vector3l(b).sub(a);

        double h = Math.clamp(dot(pa, ba) / dot(ba, ba), 0, 1);
        return new Vector3d(pa.x, pa.y, pa.z).sub(new Vector3d(ba.x, ba.y, ba.z).mul(h)).length() - radius.value() >= 0;
    }

    private static double dot(Vector3l a, Vector3l b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }


    private final byte material;
    private final ArrayList<Chunk> affectedChunks = new ArrayList<>();
    private final StandAloneIntSetting radius = new StandAloneIntSetting(1, 64, 8);

    private Vector3l startPosition, endPosition;

    private static final long[] CAPSULE_ICON_BITMAP = new long[]{
            0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0xFEA0C00000000000L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L, 0xDCFF004040C00000L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L,
            0x0L, 0xBA00FF202000A000L, 0x0L, 0x0L, 0xFEA0C00000000000L, 0xDCFF004040C00000L, 0xBA00FF202000A000L, 0xFFFFFFFF70F0F0F8L, 0xFFFFFFFFFFFFFFFEL,
            0x4FF005D5DFF00DCL, 0x200FF3B3B00FFBAL, 0x3057F7FFFFFFFL, 0xFEA0C00000000000L, 0xDCFF004040C00000L, 0xBA00FF202000A000L, 0xFFFFFFFFFFFFFFFEL,
            0xFFAACC08FFEAEC88L, 0x4FF004C5DFF00CCL, 0x200FF2A3B00FFAAL, 0x3057F7FFFFFFFL, 0x0L, 0x0L, 0x4FF005D5DFF00DCL, 0x0L, 0x0L, 0x0L, 0x50004L, 0x0L,
            0x0L, 0x200FF3B3B00FFBAL, 0x0L, 0x0L, 0x0L, 0x302L, 0x0L, 0x0L, 0x3057F7FFFFFFFL, 0x50004L, 0x302L, 0x0L, 0x0L, 0x0L, 0x0L, 0x0L};
}
