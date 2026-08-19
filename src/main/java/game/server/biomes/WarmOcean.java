package game.server.biomes;

import game.server.generation.GenerationData;

import static game.server.generation.WorldGeneration.WATER_LEVEL;
import static game.utils.Constants.SAND;

public final class WarmOcean implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        long totalX = data.totalX;
        long totalY = data.computeTotalY(inChunkY);
        long totalZ = data.totalZ;

        int sandHeight = (int) (data.feature * 64.0) + WATER_LEVEL - 80;
        if (totalY > sandHeight) data.store(inChunkX, inChunkY, inChunkZ, SAND);
        else data.store(inChunkX, inChunkY, inChunkZ, data.getWarmOceanFloorMaterial(totalX, totalY, totalZ));
    }
}
