package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

public record HomogenousSurfaceBiome(String name, StructureCollectionIdentifier structures, int structureChance,
                                     int biomeDepth, byte surfaceMaterial) implements Biome {

    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        Biome.placeHomogenousSurfaceMaterial(inChunkX, inChunkY, inChunkZ, data, surfaceMaterial);
    }

    @Override
    public int getBiomeDepth(GenerationData data) {
        return biomeDepth + data.biomeDepthMod;
    }

    @Override
    public int getStructureChancePromille() {
        return structureChance;
    }

    @Override
    public WorldGenStructure getStructure(long totalX, long height, long totalZ) {
        if (structures == null) return null;
        return Biome.getRandomStructure(totalX, height, totalZ, structures);
    }

    @Override
    public String getName() {
        return name;
    }
}
