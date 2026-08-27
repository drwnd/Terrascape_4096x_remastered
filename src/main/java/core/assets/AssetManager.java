package core.assets;

import core.assets.identifiers.*;
import core.rendering_api.Debug;
import core.utils.FileManager;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public final class AssetManager {

    private AssetManager() {
    }


    public static void deleteAll() {
        Debug.log("---Deleting old Assets---");
        synchronized (assets) {
            for (Asset asset : assets.values()) asset.delete();
            assets.clear();
        }
        for (Runnable callback : deleteAllCallbacks) callback.run();
    }

    public static void delete(AssetIdentifier<?> identifier) {
        synchronized (assets) {
            if (!assets.containsKey(identifier)) return;
            Asset asset = assets.get(identifier);
            asset.delete();
            assets.remove(identifier, asset);
        }
    }

    @SuppressWarnings("unchecked")
    public static <ASSET extends Asset> ASSET get(AssetIdentifier<ASSET> identifier) {
        synchronized (assets) {
            if (assets.containsKey(identifier)) return (ASSET) assets.get(identifier);
            ASSET asset = identifier.generateAsset();
            assets.put(identifier, asset);
            return asset;
        }
    }

    public static Path getAssetFilepath(Path assetPath) {
        for (String assetPackName : assetPackNames) {
            Path filepath = Path.of("assetPacks", assetPackName).resolve(assetPath);
            if (filepath.toFile().exists()) return filepath;
        }
        return Path.of("assetPacks", "Default").resolve(assetPath);
    }

    public static ArrayList<String> getAssetFilePathsInFolder(String folderName) {
        ArrayList<String> filePaths = new ArrayList<>();
        for (String assetPackName : assetPackNames)
            addAssetFilePathsInFolder(filePaths, "assetPacks/%s/%s".formatted(assetPackName, folderName));
        addAssetFilePathsInFolder(filePaths, "assetPacks/Default/" + folderName);
        return filePaths;
    }

    public static ArrayList<String> getAssetFilePathsInFolderMatching(String folderName, String fileNamePrefix) {
        ArrayList<String> assetFilesInFolder = getAssetFilePathsInFolder(folderName);
        assetFilesInFolder.removeIf(path -> {
            int beginIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
            String fileName = path.substring(beginIndex + 1);
            return !fileName.startsWith(fileNamePrefix);
        });
        return assetFilesInFolder;
    }

    public static void addDeleteAllCallback(Runnable callback) {
        if (callback == null) return;
        deleteAllCallbacks.add(callback);
    }

    public static ArrayList<String> getActiveAssetPackNames() {
        return new ArrayList<>(assetPackNames);
    }

    public static void setActiveAssetPackNames(ArrayList<String> assetPackNames) {
        AssetManager.assetPackNames = new ArrayList<>(assetPackNames);
        deleteAll();
    }

    private static void addAssetFilePathsInFolder(ArrayList<String> filePaths, String folderPath) {
        File[] files = FileManager.getChildren(new File(folderPath));
        if (files == null) return;
        for (File file : files) filePaths.add(file.getPath());
    }

    private static final ArrayList<Runnable> deleteAllCallbacks = new ArrayList<>();
    private static final HashMap<AssetIdentifier<?>, Asset> assets = new HashMap<>();
    private static ArrayList<String> assetPackNames = new ArrayList<>();
}
