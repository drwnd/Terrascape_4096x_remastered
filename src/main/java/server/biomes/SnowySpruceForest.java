package server.biomes;

import server.GenerationData;

import static utils.Constants.SNOW;

public final class SnowySpruceForest extends Biome {
    @Override
    public boolean placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        return Biome.placeHomogenousSurfaceMaterial(inChunkX, inChunkY, inChunkZ, data, SNOW);
    }

    @Override
    public int getRequiredTreeZeroBits() {
        return 0b010010010000;
    }
}
