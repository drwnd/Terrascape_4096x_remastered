package core.assets.identifiers;

import core.assets.AssetLoader;
import core.assets.AssetManager;
import core.assets.Texture;
import core.assets.TextureArray;
import core.rendering_api.CoreObjectLoader;
import core.utils.FileIndexSet;

public interface TextureArrayIdentifier extends AssetIdentifier<TextureArray> {

    String folderName();

    FileIndexSet indexSet();

    default TextureArray generateAsset() {
        String filepath = AssetManager.getAssetFilepath("textures/" + folderName());
        Texture[] textures = getTextures(filepath, indexSet());
        return CoreObjectLoader.generateTextureArray(textures);
    }

    private static Texture[] getTextures(String folderName, FileIndexSet indexSet) {
        Texture[] textures = new Texture[indexSet.getCount()];

        for (int index = 0; index < textures.length; index++) {
            String fileName = indexSet.getFileName(index);
            String filepath = folderName + '/' + fileName;

            textures[index] = AssetLoader.loadTexture2D(filepath);
        }
        return textures;
    }
}
