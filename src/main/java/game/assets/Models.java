package game.assets;

import com.google.gson.Gson;

import core.assets.AssetManager;
import core.assets.GuiElementData;
import core.assets.identifiers.GuiElementIdentifier;
import core.utils.FileManager;

import java.nio.file.Path;

public enum Models implements GuiElementIdentifier {
    PLAYER_MODEL;

    @Override
    public GuiElementData getData() {
        Path filepath = AssetManager.getAssetFilepath(Path.of("models", name() + ".json"));
        return new Gson().fromJson(FileManager.loadJson(filepath), GuiElementData.class);
    }
}
