package game.server.biomes;

import game.server.generation.GenerationData;

public final class Ocean implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        long totalX = data.totalX;
        long totalY = data.computeTotalY(inChunkY);
        long totalZ = data.totalZ;
        data.store(inChunkX, inChunkY, inChunkZ, data.getOceanFloorMaterial(totalX, totalY, totalZ));
    }
}
