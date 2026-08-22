package game.server.biomes;

import core.assets.AssetManager;

import core.utils.MathUtils;

import game.assets.StructureCollectionIdentifier;
import game.assets.StructureIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.Structure;
import game.server.generation.WorldGenStructure;
import game.server.generation.WorldGeneration;

import static game.utils.Constants.*;

public interface Biome {

    void placeMaterials(int inChunkX, int inChunkZ, int inChunkStartY, int inChunkEndY, GenerationData data);

    default void placeSpecialFeatures(int inChunkX, int inChunkZ, GenerationData data) {
    }

    default int getBiomeDepth(GenerationData data) {
        return 48 + data.biomeDepthMod;
    }

    default int getSpecialHeight(long totalX, long totalZ) {
        return 0;
    }

    default int getStructureChancePromille() {
        return 0;
    }

    default WorldGenStructure getStructure(long totalX, long height, long totalZ) {
        return null;
    }

    default WorldGenStructure getStructureFeature(long totalX, long height, long totalZ) {
        return new WorldGenStructure(totalX, height, totalZ, AssetManager.get(new StructureIdentifier("BluePrint")), (byte) 0);
    }

    default String getName() {
        return getClass().getSimpleName();
    }


    static WorldGenStructure getRandomStructure(long x, long y, long z, StructureCollectionIdentifier structures) {
        byte transform = (byte) (MathUtils.hash((int) x >>> CHUNK_SIZE_BITS, (int) z >>> CHUNK_SIZE_BITS, (int) WorldGeneration.SEED ^ 0xEB0A8449) & Structure.ALL_TRANSFORMS);
        return new WorldGenStructure(x, y, z, AssetManager.get(structures).getRandom((int) x, (int) y, (int) z), transform);
    }
}