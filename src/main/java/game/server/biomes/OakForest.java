package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

import static game.utils.Constants.DIRT;
import static game.utils.Constants.GRASS;

public final class OakForest implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        Biome.placeLayeredSurfaceMaterial(inChunkX, inChunkY, inChunkZ, data, 8, GRASS, DIRT);
    }

    @Override
    public WorldGenStructure getStructure(long totalX, long height, long totalZ) {
        return Biome.getRandomStructure(totalX, height, totalZ, StructureCollectionIdentifier.OAK_TREES);
    }

    @Override
    public int getStructureChancePromille() {
        return 128;
    }
}
