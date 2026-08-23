package game.assets;

import core.assets.AssetManager;
import core.assets.identifiers.AssetIdentifier;
import core.utils.MathUtils;

import game.server.generation.Structure;

import java.io.File;
import java.util.ArrayList;

import static game.utils.Constants.CHUNK_SIZE_BITS;

public enum StructureCollectionIdentifier implements AssetIdentifier<StructureCollection> {
    OAK_TREES("OakTree"),
    SPRUCE_TREES("SpruceTree"),
    DARK_OAK_TREES("DarkOakTree"),
    PINE_TREES("PineTree"),
    REDWOOD_TREES("RedwoodTree"),
    BLACK_WOOD_TREES("BlackWoodTree"),
    CACTUS("Cactus"),
    SHRUB("Shrub");

    StructureCollectionIdentifier(String structureBaseName) {
        this.structureBaseName = structureBaseName.toLowerCase();
    }

    @Override
    public StructureCollection generateAsset() {
        ArrayList<String> structureFilePaths = AssetManager.getAssetFilePathsInFolder("structures");
        ArrayList<Structure> structuresList = new ArrayList<>();

        for (String structureFilepath : structureFilePaths) {
            String structureName = new File(structureFilepath).getName().toLowerCase();
            if (!structureName.startsWith(structureBaseName)) continue;
            Structure structure = AssetManager.get(new StructureIdentifier(structureName));
            structuresList.add(structure);
        }

        Structure[] structures = new Structure[structuresList.size()];
        for (int index = 0; index < structures.length; index++)
            structures[index] = structuresList.get(index);

        return new StructureCollection(structures);
    }

    public static AssetIdentifier<StructureCollection> merge(StructureCollectionIdentifier[] identifiers, float[] weights) {
        return new MergedStructureCollectionIdentifier(identifiers, weights);
    }

    private final String structureBaseName;


    private record MergedStructureCollectionIdentifier(StructureCollectionIdentifier[] identifiers,
                                                       float[] weights) implements AssetIdentifier<StructureCollection> {

        @Override
        public StructureCollection generateAsset() {
            ArrayList<String> structureFilePaths = AssetManager.getAssetFilePathsInFolder("structures");
            ArrayList<Structure> structuresList = new ArrayList<>();
            Structure[][] structures = new Structure[identifiers().length][0];

            for (int identifierIndex = 0, identifiersLength = identifiers.length; identifierIndex < identifiersLength; identifierIndex++) {
                structuresList.clear();
                StructureCollectionIdentifier identifier = identifiers[identifierIndex];
                for (String structureFilepath : structureFilePaths) {
                    String structureName = new File(structureFilepath).getName().toLowerCase();
                    if (!structureName.startsWith(identifier.structureBaseName)) continue;
                    Structure structure = AssetManager.get(new StructureIdentifier(structureName));
                    structuresList.add(structure);
                }

                structures[identifierIndex] = new Structure[structuresList.size()];
                for (int index = 0; index < structures.length; index++)
                    structures[identifierIndex][index] = structuresList.get(index);
            }

            return new MergedStructureCollection(structures, weights);
        }
    }

    private static final class MergedStructureCollection extends StructureCollection {

        private MergedStructureCollection(Structure[][] structures, float[] weights) {
            super(null);
            this.structures = structures;
            this.weights = weights;
            if (structures.length != weights.length) throw new IllegalArgumentException("Structure and weight Arrays must be the same length!");
        }

        @Override
        public Structure getRandom(int x, int y, int z) {
            float random = (MathUtils.hash(x, z, y) & 1023) / 1023F, weight = 0.0F;

            for (int index = 0; index < weights.length; index++) {
                weight += weights[index];
                int length = this.structures[index].length;
                if (random <= weight) return this.structures[index][(x + y + z >>> CHUNK_SIZE_BITS) % length];
            }

            return null;
        }

        private final Structure[][] structures;
        private final float[] weights;
    }
}