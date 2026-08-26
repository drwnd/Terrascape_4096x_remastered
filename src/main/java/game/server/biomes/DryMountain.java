package game.server.biomes;

import core.utils.MathUtils;

import game.server.generation.GenerationData;

import static game.server.generation.WorldGeneration.WATER_LEVEL;

public final class DryMountain implements Biome {
    @Override
    public void placeMaterials(int inChunkX, int inChunkZ, int inChunkStartY, int inChunkEndY, GenerationData data) {
        int dirtHeight = MathUtils.floor(data.feature * 512 + WATER_LEVEL);
        if (data.height > dirtHeight) return;
        data.storeColumn(inChunkX, inChunkZ, inChunkStartY, inChunkEndY, GenerationData::getGeneratingDirtType);
    }
}
