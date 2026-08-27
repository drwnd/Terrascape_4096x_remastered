package core.assets.identifiers;

import core.assets.AssetLoader;
import core.assets.AssetManager;
import core.assets.Texture;

import java.nio.file.Path;

public interface TextureIdentifier extends AssetIdentifier<Texture> {

    String fileName();

    default Texture generateAsset() {
        Path filepath = AssetManager.getAssetFilepath(Path.of("textures", fileName()));
        return AssetLoader.loadTexture2D(filepath);
    }
}
