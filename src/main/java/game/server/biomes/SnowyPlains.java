package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.Tree;

import static game.utils.Constants.SNOW;

public final class SnowyPlains implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        Biome.placeHomogenousSurfaceMaterial(inChunkX, inChunkY, inChunkZ, data, SNOW);
    }

    @Override
    public Tree getGeneratingTree(long totalX, long height, long totalZ) {
        return Biome.getRandomTree(totalX, height, totalZ, StructureCollectionIdentifier.SPRUCE_TREES);
    }

    @Override
    public int getRequiredTreeZeroBits() {
        return 0b01001010010010000;
    }
}
