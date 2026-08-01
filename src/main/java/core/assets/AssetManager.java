package core.assets;

import core.assets.identifiers.*;
import core.rendering_api.Debug;
import core.utils.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public final class AssetManager {

    private AssetManager() {
    }


    public static void deleteAll() {
        Debug.log("---Deleting old Assets---");
        synchronized (assets) {
            for (Asset asset : assets.values()) asset.delete();
            assets.clear();
        }
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

    public static String getAssetFilepath(String assetName) {
        for (String assetPackName : assetPackNames) {
            String filepath = "assetPacks/%s/%s".formatted(assetPackName, assetName);
            if (new File(filepath).exists()) return filepath;
        }
        return "assetPacks/Default/" + assetName;
    }

    public static ArrayList<String> getAssetPackNames() {
        return new ArrayList<>(assetPackNames);
    }

    public static void setAssetPackNames(ArrayList<String> assetPackNames) {
        AssetManager.assetPackNames = new ArrayList<>(assetPackNames);
    }

    public static Set<String> getAssetFilePathsInFolder(String folderName) {
        Set<String> filePaths = new TreeSet<>();
        for (String assetPackName : assetPackNames)
            addAssetFilePathsInFolder(filePaths, "assetPacks/%s/%s".formatted(assetPackName, folderName));
        addAssetFilePathsInFolder(filePaths, "assetPacks/Default/" + folderName);
        return filePaths;
    }


    private static void addAssetFilePathsInFolder(Set<String> filePaths, String folderPath) {
        File[] files = FileManager.getChildren(new File(folderPath));
        if (files == null) return;
        for (File file : files) filePaths.add(file.getPath());
    }

    private static final HashMap<AssetIdentifier<?>, Asset> assets = new HashMap<>();
    private static ArrayList<String> assetPackNames = new ArrayList<>();
}
