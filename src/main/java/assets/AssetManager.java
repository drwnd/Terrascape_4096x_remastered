package assets;

import assets.identifiers.*;
import rendering_api.ObjectLoader;
import rendering_api.shaders.Shader;
import rendering_api.ShaderLoader;

import java.util.HashMap;

public final class AssetManager {

    private AssetManager() {
    }


    public static Texture getTexture(TextureIdentifier identifier) {
        if (assets.containsKey(identifier.getIdentifier())) return (Texture) assets.get(identifier.getIdentifier());
        Texture texture = ObjectLoader.loadTexture(identifier.getIdentifier());
        assets.put(identifier.getIdentifier(), texture);
        return texture;
    }

    public static GuiElement getGuiElement(GuiElementIdentifier identifier) {
        if (assets.containsKey(identifier.getIdentifier())) return (GuiElement) assets.get(identifier.getIdentifier());
        GuiElement guiElement = ObjectLoader.loadGuiElement(identifier);
        assets.put(identifier.getIdentifier(), guiElement);
        return guiElement;
    }

    public static Shader getShader(ShaderIdentifier identifier) {
        if (assets.containsKey(identifier.getIdentifier())) return (Shader) assets.get(identifier.getIdentifier());
        Shader shader = ShaderLoader.loadShader(identifier);
        assets.put(identifier.getIdentifier(), shader);
        return shader;
    }

    public static Buffer getBuffer(BufferIdentifier identifier) {
        if (assets.containsKey(identifier.getIdentifier())) return (Buffer) assets.get(identifier.getIdentifier());
        Buffer buffer = new Buffer(identifier.getGenerator());
        assets.put(identifier.getIdentifier(), buffer);
        return buffer;
    }

    public static VertexArray getVertexArray(VertexArrayIdentifier identifier) {
        if (assets.containsKey(identifier.getIdentifier())) return (VertexArray) assets.get(identifier.getIdentifier());
        VertexArray vertexArray = new VertexArray(identifier.getGenerator());
        assets.put(identifier.getIdentifier(), vertexArray);
        return vertexArray;
    }


    public static void reload() {
        System.out.println("---Reloading all Assets---");
        for (String identifier : assets.keySet()) assets.get(identifier).reload(identifier);
        System.out.println("---Reloaded all Assets ---");
    }

    private static final HashMap<String, Asset> assets = new HashMap<>();
}
