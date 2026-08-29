package game.assets;

import com.google.gson.Gson;

import core.assets.AssetManager;
import core.assets.identifiers.AssetIdentifier;
import core.utils.FileManager;

import java.nio.file.Path;

public enum Models implements AssetIdentifier<Model> {
    PLAYER_MODEL;

    @Override
    public Model generateAsset() {
        Path filepath = AssetManager.getAssetFilepath(Path.of("models", name() + ".json"));
        Model.ModelData modelData = new Gson().fromJson(FileManager.loadJson(filepath), Model.ModelData.class);
        return new Model(modelData);
    }
}
