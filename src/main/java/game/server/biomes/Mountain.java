package game.server.biomes;

import core.utils.MathUtils;

import game.server.generation.GenerationData;

import static game.server.generation.WorldGeneration.WATER_LEVEL;
import static game.utils.Constants.*;

public final class Mountain implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        long totalY = data.totalY;
        int snowHeight = MathUtils.floor(data.feature * 512 + SNOW_LEVEL);
        int grassHeight = MathUtils.floor(data.feature * 512) + WATER_LEVEL;

        if (totalY > snowHeight) data.store(inChunkX, inChunkY, inChunkZ, SNOW);
        else if (data.isInsideSurfaceMaterialLevel(totalY, 8) && data.height <= grassHeight) data.store(inChunkX, inChunkY, inChunkZ, GRASS);
        else if (data.height <= grassHeight) data.store(inChunkX, inChunkY, inChunkZ, DIRT);
    }

    private static final int SNOW_LEVEL = WATER_LEVEL + 1456;
}
