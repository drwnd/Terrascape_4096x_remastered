package game.server.biomes;

import game.server.generation.GenerationData;

import static game.utils.Constants.*;

public final class Mesa implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        Biome.placeLayeredSurfaceMaterial(inChunkX, inChunkY, inChunkZ, data, 48, RED_SAND, RED_SANDSTONE);
    }

    @Override
    public int getBiomeDepth(GenerationData data) {
        return 128 + data.biomeDepthMod;
    }
}
