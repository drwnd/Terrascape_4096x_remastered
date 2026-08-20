package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

import static game.utils.Constants.DIRT;

public final class RedwoodForest implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        long totalX = data.totalX;
        long totalY = data.computeTotalY(inChunkY);
        long totalZ = data.totalZ;

        if (data.isInsideSurfaceMaterialLevel(totalY, 8)) data.store(inChunkX, inChunkY, inChunkZ, data.getGeneratingGrassType(totalX, totalZ, totalZ));
        else data.store(inChunkX, inChunkY, inChunkZ, DIRT);
    }

    @Override
    public WorldGenStructure getStructure(long totalX, long height, long totalZ) {
        return Biome.getRandomStructure(totalX, height, totalZ, StructureCollectionIdentifier.REDWOOD_TREES);
    }

    @Override
    public int getStructureChancePromille() {
        return 128;
    }
}
