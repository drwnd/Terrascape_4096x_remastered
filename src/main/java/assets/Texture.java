package assets;

import org.lwjgl.opengl.GL46;
import rendering_api.ObjectLoader;

public final class Texture extends Asset {

    public Texture(int textureID) {
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    @Override
    public void reload(String identifier) {
        System.out.printf("Reloading Texture %s%n", identifier);
        GL46.glDeleteTextures(textureID);
        textureID = ObjectLoader.loadTexture(identifier).textureID;
    }

    private int textureID;
}
