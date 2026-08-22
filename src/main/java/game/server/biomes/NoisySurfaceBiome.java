package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

public class NoisySurfaceBiome implements Biome {

    public NoisySurfaceBiome(String name,
                             StructureCollectionIdentifier structures, int structureChance,
                             StructureCollectionIdentifier structureFeatures, int structureFeatureChance,
                             int biomeDepth, MaterialFunction materialFunction) {
        this.name = name;
        this.structures = structures;
        this.structureChance = structureChance;
        this.structureFeatures = structureFeatures;
        this.structureFeatureChance = structureFeatureChance;
        this.biomeDepth = biomeDepth;
        this.materialFunction = materialFunction;
    }

    @Override
    public void placeMaterials(int inChunkX, int inChunkZ, int inChunkStartY, int inChunkEndY, GenerationData data) {
        for (int inChunkY = inChunkStartY; inChunkY < inChunkEndY; inChunkY++)
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
    private final StructureCollectionIdentifier structures, structureFeatures;
    private final int structureChance, structureFeatureChance, biomeDepth;
    private final MaterialFunction materialFunction;

    public interface MaterialFunction {
        byte getGeneratingMaterial(GenerationData data, long x, long y, long z);
    }
}
