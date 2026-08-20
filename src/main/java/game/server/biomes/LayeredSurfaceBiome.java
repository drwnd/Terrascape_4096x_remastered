package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

public record LayeredSurfaceBiome(String name, StructureCollectionIdentifier structures, int structureChance,
                                  int surfaceMaterialDepth, int biomeDepth, byte topMaterial, byte bottomMaterial) implements Biome {

    public static LayeredSurfaceBiome withName(String name, LayeredSurfaceBiome biome) {
        return new LayeredSurfaceBiome(name, biome.structures, biome.structureChance, biome.surfaceMaterialDepth, biome.biomeDepth, biome.topMaterial, biome.bottomMaterial);
    }

    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        Biome.placeLayeredSurfaceMaterial(inChunkX, inChunkY, inChunkZ, data, surfaceMaterialDepth, topMaterial, bottomMaterial);
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
