package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

import static game.utils.Constants.*;

public final class BlackWoodForest implements Biome {
    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        Biome.placeLayeredSurfaceMaterial(inChunkX, inChunkY, inChunkZ, data, 8, PODZOL, DIRT);
    }

    @Override
    public WorldGenStructure getStructure(long totalX, long height, long totalZ) {
        return Biome.getRandomStructure(totalX, height, totalZ, StructureCollectionIdentifier.BLACK_WOOD_TREES);
    }

    @Override
    public int getStructureChancePromille() {
        return 128;
    }
}
