package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

public class HomogenousSurfaceBiome implements Biome {

    public HomogenousSurfaceBiome(String name, StructureCollectionIdentifier structures, int structureChance, int biomeDepth, byte surfaceMaterial) {
        this.name = name;
        this.structures = structures;
        this.structureChance = structureChance;
        this.biomeDepth = biomeDepth;
        this.surfaceMaterial = surfaceMaterial;
    }

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

    private final String name;
    private final StructureCollectionIdentifier structures;
    private final int structureChance, biomeDepth;
    private final byte surfaceMaterial;
}
