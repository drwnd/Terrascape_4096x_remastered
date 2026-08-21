package game.server.biomes;

import core.utils.MathUtils;

import game.server.generation.GenerationData;

import static game.server.generation.WorldGeneration.WATER_LEVEL;
import static game.utils.Constants.SNOW;

public final class SnowyMountain implements Biome {
    @Override
    public void placeMaterials(int inChunkX, int inChunkZ, int inChunkStartY, int inChunkEndY, GenerationData data) {
        int iceHeight = data.clampStartHeightToInChunkY(MathUtils.floor(data.feature * 512 + ICE_LEVEL));
        data.storeColumn(inChunkX, inChunkZ, inChunkStartY, Math.min(inChunkEndY, iceHeight), SNOW);
        for (int inChunkY = Math.max(inChunkStartY, iceHeight); inChunkY < inChunkEndY; inChunkY++)
            data.store(inChunkX, inChunkY, inChunkZ, data.getGeneratingIceType(data.totalX, data.computeTotalY(inChunkY), data.totalZ));
    }

    @Override
    public int getBiomeDepth(GenerationData data) {
        return 48;
    }

    private static final int ICE_LEVEL = WATER_LEVEL + 2256;
}
