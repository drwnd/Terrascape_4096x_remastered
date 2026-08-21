package game.server.biomes;

import core.utils.MathUtils;

import game.server.generation.GenerationData;

import static game.server.generation.WorldGeneration.WATER_LEVEL;

public final class DryMountain implements Biome {
    @Override
    public void placeMaterials(int inChunkX, int inChunkZ, int inChunkStartY, int inChunkEndY, GenerationData data) {
        int dirtHeight = MathUtils.floor(data.feature * 512 + WATER_LEVEL);
        if (data.height > dirtHeight) return;
        for (int inChunkY = inChunkStartY; inChunkY < inChunkEndY; inChunkY++)
            data.store(inChunkX, inChunkY, inChunkZ, data.getGeneratingDirtType(data.totalX, data.computeTotalY(inChunkY), data.totalZ));
    }
}
