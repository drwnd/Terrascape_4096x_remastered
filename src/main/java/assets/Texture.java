package assets;

import org.lwjgl.opengl.GL46;
import rendering_api.ObjectLoader;

public final class Texture extends Asset {

    public Texture(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    @Override
    public void reload(String identifier) {
        System.out.printf("Reloading Texture %s%n", identifier);
        GL46.glDeleteTextures(id);
        id = ObjectLoader.loadTexture(identifier).id;
    }

    private int id;
}
