package game.assets;

import core.assets.AssetManager;
import core.assets.identifiers.AssetIdentifier;
import game.server.generation.Structure;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public enum StructureCollectionIdentifier implements AssetIdentifier<StructureCollection> {
    OAK_TREES("OakTree"),
    SPRUCE_TREES("SpruceTree"),
    DARK_OAK_TREES("DarkOakTree"),
    PINE_TREES("PineTree"),
    REDWOOD_TREES("RedwoodTree"),
    BLACK_WOOD_TREES("BlackWoodTree");

    StructureCollectionIdentifier(String structureBaseName) {
        this.structureBaseName = structureBaseName.toLowerCase();
    }

    @Override
    public StructureCollection generateAsset() {
        Set<String> structureFilePaths = AssetManager.getAssetFilePathsInFolder("structures");
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

    private final String structureBaseName;
}
