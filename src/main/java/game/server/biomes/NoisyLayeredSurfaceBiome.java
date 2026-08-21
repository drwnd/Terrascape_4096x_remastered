package game.server.biomes;

import game.assets.StructureCollectionIdentifier;
import game.server.generation.GenerationData;
import game.server.generation.WorldGenStructure;

public class NoisyLayeredSurfaceBiome implements Biome {

    public NoisyLayeredSurfaceBiome(String name, StructureCollectionIdentifier structures, int structureChance, int surfaceMaterialDepth, int biomeDepth, byte bottomMaterial, NoisySurfaceBiome.MaterialFunction materialFunction) {
        this.name = name;
        this.structures = structures;
        this.structureChance = structureChance;
        this.biomeDepth = biomeDepth;
        this.surfaceMaterialDepth = surfaceMaterialDepth;
        this.bottomMaterial = bottomMaterial;
        this.materialFunction = materialFunction;
    }

    @Override
    public void placeMaterial(int inChunkX, int inChunkY, int inChunkZ, GenerationData data) {
        long totalX = data.totalX;
        long totalY = data.computeTotalY(inChunkY);
        long totalZ = data.totalZ;

        boolean insideSurfaceMaterialLevel = data.isInsideSurfaceMaterialLevel(totalY, surfaceMaterialDepth);
        byte material = insideSurfaceMaterialLevel ? materialFunction.getGeneratingMaterial(data, totalX, totalY, totalZ) : bottomMaterial;
        data.store(inChunkX, inChunkY, inChunkZ, material);
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
    private final int structureChance, biomeDepth, surfaceMaterialDepth;
    private final byte bottomMaterial;
    private final NoisySurfaceBiome.MaterialFunction materialFunction;
}
