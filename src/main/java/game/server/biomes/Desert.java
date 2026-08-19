package game.server.biomes;

import game.server.generation.GenerationData;

import static game.utils.Constants.SAND;
import static game.utils.Constants.SANDSTONE;

public final class Desert implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        long totalY = data.totalY;

        int floorMaterialDepth = 48 + data.floorMaterialDepthMod;
        if (data.isBelowFloorMaterialLevel(totalY, floorMaterialDepth)) data.store(inChunkX, inChunkY, inChunkZ, SANDSTONE);
        else data.store(inChunkX, inChunkY, inChunkZ, SAND);
    }

    @Override
    public int getFloorMaterialDepth(GenerationData data) {
        return 128 + data.floorMaterialDepthMod;
    }
}
