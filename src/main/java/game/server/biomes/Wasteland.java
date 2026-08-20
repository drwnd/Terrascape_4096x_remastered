package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

public final class Wasteland implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        long totalX = data.totalX;
        long totalY = data.computeTotalY(inChunkY);
        long totalZ = data.totalZ;
        data.store(inChunkX, inChunkY, inChunkZ, data.getGeneratingDirtType(totalX, totalY, totalZ));
    }

    @Override
    public WorldGenStructure getStructure(long totalX, long height, long totalZ) {
        return Biome.getRandomStructure(totalX, height, totalZ, StructureCollectionIdentifier.BLACK_WOOD_TREES);
    }

    @Override
    public int getStructureChancePromille() {
        return 8;
    }
}
