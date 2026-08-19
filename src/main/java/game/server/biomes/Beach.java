package game.server.biomes;

import game.server.generation.GenerationData;

import static game.utils.Constants.SAND;

public final class Beach implements Biome {

    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        Biome.placeHomogenousSurfaceMaterial(inChunkX, inChunkY, inChunkZ, data, SAND);
    }
}
