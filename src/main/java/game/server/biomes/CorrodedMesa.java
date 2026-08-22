package game.server.biomes;

import game.server.generation.GenerationData;

import core.utils.OpenSimplex2S;

import static game.utils.Constants.*;
import static game.server.generation.WorldGeneration.SEED;

public final class CorrodedMesa extends LayeredSurfaceBiome {
    public CorrodedMesa() {
        super("Corroded Mesa",
                null, 0,
                null, 0,
                48, 128, RED_SAND, RED_SANDSTONE);
    }

    @Override
    public void placeSpecialFeatures(int inChunkX, int inChunkZ, GenerationData data) {
        int pillarHeight = data.specialHeight;
        if (pillarHeight == 0) return;
        int start = data.clampStartHeightToInChunkY(data.height - data.biomeDepth);
        int end = data.clampEndHeightToInChunkY(data.height + pillarHeight);

        for (int inChunkY = start; inChunkY < end; inChunkY++)
            data.store(inChunkX, inChunkY, inChunkZ, getGeneratingTerracottaType((int) (data.computeTotalY(inChunkY) >> 4 & 15)));
    }

    @Override
    public int getSpecialHeight(long totalX, long totalZ) {
        double noise = OpenSimplex2S.noise2(SEED ^ 0xDF860F2E2A604A17L, totalX * MESA_PILLAR_FREQUENCY, totalZ * MESA_PILLAR_FREQUENCY);
        noise += OpenSimplex2S.noise2(SEED ^ 0x3B632CA2452D2CCDL, totalX * MESA_PILLAR_FREQUENCY * 10, totalZ * MESA_PILLAR_FREQUENCY * 10) * 0.075;
        if (Math.abs(noise) > MESA_PILLAR_THRESHOLD) return MESA_PILLAR_HEIGHT;
        return 0;
    }

    private static byte getGeneratingTerracottaType(int terracottaIndex) {
        return switch (terracottaIndex) {
            case 3, 6, 10, 11, 15 -> RED_TERRACOTTA;
            case 2, 8, 12 -> YELLOW_TERRACOTTA;
            default -> TERRACOTTA;
        };
    }

    private static final double MESA_PILLAR_THRESHOLD = 0.55;
    private static final double MESA_PILLAR_FREQUENCY = 1 / 516.0;
    private static final int MESA_PILLAR_HEIGHT = 400;
}
