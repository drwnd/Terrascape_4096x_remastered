package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

public class LayeredSurfaceBiome implements Biome {

    public LayeredSurfaceBiome(String name, StructureCollectionIdentifier structures, int structureChance, int surfaceMaterialDepth, int biomeDepth, byte topMaterial, byte bottomMaterial) {
        this.name = name;
        this.structures = structures;
        this.structureChance = structureChance;
        this.surfaceMaterialDepth = surfaceMaterialDepth;
        this.biomeDepth = biomeDepth;
        this.topMaterial = topMaterial;
        this.bottomMaterial = bottomMaterial;
    }

    @Override
    public void placeMaterials(int inChunkX, int inChunkZ, int inChunkStartY, int inChunkEndY, GenerationData data) {
        int surfaceMaterialStart = data.clampStartHeightToInChunkY(data.height - surfaceMaterialDepth - data.biomeDepthMod);
        data.storeColumn(inChunkX, inChunkZ, inChunkStartY, surfaceMaterialStart, bottomMaterial);
        data.storeColumn(inChunkX, inChunkZ, surfaceMaterialStart, inChunkEndY, topMaterial);
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
    private final int structureChance, surfaceMaterialDepth, biomeDepth;
    private final byte topMaterial, bottomMaterial;
}
