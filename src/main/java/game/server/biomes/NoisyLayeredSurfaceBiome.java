package game.server.biomes;

import core.assets.identifiers.AssetIdentifier;
import game.assets.StructureCollection;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

public class NoisyLayeredSurfaceBiome implements Biome {

    public NoisyLayeredSurfaceBiome(String name,
                                    AssetIdentifier<StructureCollection> structures, int structureChance,
                                    AssetIdentifier<StructureCollection> structureFeatures, int structureFeatureChance,
                                    int surfaceMaterialDepth, int biomeDepth, byte bottomMaterial, NoisySurfaceBiome.MaterialFunction materialFunction) {
        this.name = name;
        this.structures = structures;
        this.structureChance = structureChance;
        this.structureFeatures = structureFeatures;
        this.structureFeatureChance = structureFeatureChance;
        this.biomeDepth = biomeDepth;
        this.surfaceMaterialDepth = surfaceMaterialDepth;
        this.bottomMaterial = bottomMaterial;
        this.materialFunction = materialFunction;
    }

    @Override
    public void placeMaterials(int inChunkX, int inChunkZ, int inChunkStartY, int inChunkEndY, GenerationData data) {
        int surfaceMaterialStart = data.clampStartHeightToInChunkY(data.height - surfaceMaterialDepth - data.biomeDepthMod);
        data.storeColumn(inChunkX, inChunkZ, inChunkStartY, surfaceMaterialStart, bottomMaterial);
        for (int inChunkY = surfaceMaterialStart; inChunkY < inChunkEndY; inChunkY++)
            data.store(inChunkX, inChunkY, inChunkZ, materialFunction.getGeneratingMaterial(data, data.totalX, data.computeTotalY(inChunkY), data.totalZ));
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
        return Biome.getRandomStructure(totalX, height, totalZ, structures);
    }

    @Override
    public int getStructureFeatureChancePromille() {
        return structureFeatureChance;
    }

    @Override
    public WorldGenStructure getStructureFeature(long totalX, long height, long totalZ) {
        if (structureFeatures == null) return null;
        return Biome.getRandomStructure(totalX, height, totalZ, structureFeatures);
    }

    @Override
    public String getName() {
        return name;
    }

    private final String name;
    private final AssetIdentifier<StructureCollection> structures, structureFeatures;
    private final int structureChance, structureFeatureChance, surfaceMaterialDepth, biomeDepth;
    private final byte bottomMaterial;
    private final NoisySurfaceBiome.MaterialFunction materialFunction;
}
