package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

import static game.utils.Constants.SNOW;

public final class SnowyPlains implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        Biome.placeHomogenousSurfaceMaterial(inChunkX, inChunkY, inChunkZ, data, SNOW);
    }

    @Override
    public WorldGenStructure getStructure(long totalX, long height, long totalZ) {
        return Biome.getRandomStructure(totalX, height, totalZ, StructureCollectionIdentifier.SPRUCE_TREES);
    }

    @Override
    public int getStructureChancePromille() {
        return 32;
    }
}
