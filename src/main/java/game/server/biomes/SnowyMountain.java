package game.server.biomes;

import core.utils.MathUtils;

import game.server.generation.GenerationData;

import static game.server.generation.WorldGeneration.WATER_LEVEL;
import static game.utils.Constants.SNOW;

public final class SnowyMountain implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        long totalX = data.totalX;
        long totalY = data.computeTotalY(inChunkY);
        long totalZ = data.totalZ;

        int iceHeight = MathUtils.floor(data.feature * 512 + ICE_LEVEL);
        if (totalY > iceHeight) data.store(inChunkX, inChunkY, inChunkZ, data.getGeneratingIceType(totalX, totalY, totalZ));
        else data.store(inChunkX, inChunkY, inChunkZ, SNOW);
    }

    private static final int ICE_LEVEL = WATER_LEVEL + 2256;
}
