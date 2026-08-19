package game.server.biomes;

import core.assets.AssetManager;

import core.utils.MathUtils;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.Structure;
import game.server.generation.Tree;
import game.server.generation.WorldGeneration;

import static game.utils.Constants.*;

public interface Biome {

    void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data);

    default int getFloorMaterialDepth(GenerationData data) {
        return 48 + data.floorMaterialDepthMod;
    }

    default int getSpecialHeight(long totalX, long totalZ) {
        return 0;
    }

    default int getRequiredTreeZeroBits() {
        return 0;
    }

    default Tree getGeneratingTree(long totalX, long height, long totalZ) {
        return null;
    }

    default String getName() {
        return getClass().getSimpleName();
    }

    static Tree getRandomTree(long x, long y, long z, StructureCollectionIdentifier trees) {
        byte transform = (byte) (MathUtils.hash((int) x >>> CHUNK_SIZE_BITS, (int) z >>> CHUNK_SIZE_BITS, (int) WorldGeneration.SEED ^ 0xEB0A8449) & Structure.ALL_TRANSFORMS);
        return new Tree(x, y, z, AssetManager.get(trees).getRandom((int) x, (int) y, (int) z), transform);
    }

    static void placeHomogenousSurfaceMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data, byte material) {
        data.store(inChunkX, inChunkY, inChunkZ, material);
    }

    static void placeLayeredSurfaceMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data, byte topMaterial) {
        long totalY = data.totalY;
        if (data.isInsideSurfaceMaterialLevel(totalY, 8)) data.store(inChunkX, inChunkY, inChunkZ, topMaterial);
        else data.store(inChunkX, inChunkY, inChunkZ, DIRT);
    }
}