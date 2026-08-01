package core.assets.identifiers;

import core.assets.AssetLoader;
import core.assets.AssetManager;
import core.assets.Texture;

public interface TextureIdentifier extends AssetIdentifier<Texture> {

    String fileName();

    default Texture generateAsset() {
        String filepath = AssetManager.getAssetFilepath("textures/" + fileName());
        return AssetLoader.loadTexture2D(filepath);
    }
}
