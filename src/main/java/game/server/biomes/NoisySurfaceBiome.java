package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

public class NoisySurfaceBiome implements Biome {

    public NoisySurfaceBiome(String name, StructureCollectionIdentifier structures, int structureChance, int biomeDepth, MaterialFunction materialFunction) {
        this.name = name;
        this.structures = structures;
        this.structureChance = structureChance;
        this.biomeDepth = biomeDepth;
        this.materialFunction = materialFunction;
    }

    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
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
    public String getName() {
        return name;
    }

    private final String name;
    private final StructureCollectionIdentifier structures;
    private final int structureChance, biomeDepth;
    private final MaterialFunction materialFunction;

    public interface MaterialFunction {
        byte getGeneratingMaterial(GenerationData data, long x, long y, long z);
    }
}
