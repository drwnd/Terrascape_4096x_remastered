package game.server.biomes;

import game.server.generation.GenerationData;

import static game.utils.Constants.RED_SAND;
import static game.utils.Constants.RED_SANDSTONE;

public final class Mesa implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        long totalY = data.computeTotalY(inChunkY);
        int floorMaterialDepth = 48 + data.floorMaterialDepthMod;
        if (data.isBelowFloorMaterialLevel(totalY, floorMaterialDepth)) data.store(inChunkX, inChunkY, inChunkZ, RED_SANDSTONE);
        else data.store(inChunkX, inChunkY, inChunkZ, RED_SAND);
    }

    @Override
    public int getFloorMaterialDepth(GenerationData data) {
        return 128 + data.floorMaterialDepthMod;
    }
}
