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
    public void placeMaterials(int inChunkX, int inChunkZ, int inChunkStartY, int inChunkEndY, GenerationData data) {
        data.storeColumn(inChunkX, inChunkZ, inChunkStartY, inChunkEndY, surfaceMaterial);
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
