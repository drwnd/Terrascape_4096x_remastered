package game.server.saving;

import core.assets.AssetManager;
import core.utils.Saver;

import game.server.materials_data.MaterialsData;
import game.server.generation.Structure;

import java.nio.file.Path;

import static game.utils.Constants.*;

public final class StructureSaver extends Saver<Structure> {

    public static Path getSaveFileLocation(String structureName) {
        return AssetManager.getAssetFilepath(Path.of("structures", structureName));
    }

    @Override
    protected void save(Structure structure) {
        saveInt(structure.sizeX());
        saveInt(structure.sizeY());
        saveInt(structure.sizeZ());
        saveInt(structure.centerX());
        saveInt(structure.centerY());
        saveInt(structure.centerZ());
        saveInt(structure.materials().getTotalSizeBits());
        saveByteArray(structure.materials().getBytes());
    }

    @Override
    protected Structure load() {
        int sizeX = loadInt();
        int sizeY = loadInt();
        int sizeZ = loadInt();
        int centerX = loadInt();
        int centerY = loadInt();
        int centerZ = loadInt();
        int totalSizeBits = loadInt();
        byte[] data = loadByteArray();

        MaterialsData materialsData = new MaterialsData(totalSizeBits, data);
        materialsData.recomputeTypes();
        return new Structure(sizeX, sizeY, sizeZ, centerX, centerY, centerZ, materialsData);
    }

    @Override
    protected Structure loadOldVersion(int versionNumber) {
        if (versionNumber == 1) {
            int sizeX = loadInt();
            int sizeY = loadInt();
            int sizeZ = loadInt();
            int totalSizeBits = loadInt();
            byte[] data = loadByteArray();

            MaterialsData materialsData = new MaterialsData(totalSizeBits, data);
            materialsData.recomputeTypes();
            return new Structure(sizeX, sizeY, sizeZ, sizeX >> 1, 0, sizeZ >> 1, materialsData);
        }
        return super.loadOldVersion(versionNumber);
    }

    @Override
    protected Structure getDefault() {
        return new Structure(OUT_OF_WORLD);
    }

    @Override
    protected int getVersionNumber() {
        return 2;
    }
}
