package core.assets.identifiers;

import core.assets.AssetLoader;
import core.assets.AssetManager;
import core.assets.Texture;
import core.assets.TextureArray;
import core.rendering_api.CoreObjectLoader;
import core.utils.FileIndexSet;

public interface TextureArrayIdentifier extends AssetIdentifier<TextureArray> {

    String folderName();

    FileIndexSet<?> indexSet();

    default TextureArray generateAsset() {
        Texture[] textures = getTextures(indexSet());
        return CoreObjectLoader.generateTextureArray(textures);
    }

    private Texture[] getTextures(FileIndexSet<?> indexSet) {
        Texture[] textures = new Texture[indexSet.getCount()];

        for (int index = 0; index < textures.length; index++) {
            String fileName = indexSet.getFileName(index);
            String filepath = AssetManager.getAssetFilepath("textures/%s/%s".formatted(folderName(), fileName));

            textures[index] = AssetLoader.loadTexture2D(filepath);
        }
        return textures;
    }
}
