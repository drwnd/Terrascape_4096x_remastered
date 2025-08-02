package assets;

import rendering_api.ObjectLoader;

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


    public static void reload() {
        for (String identifier : assets.keySet()) assets.get(identifier).reload(identifier);
    }

    private static final HashMap<String, Asset> assets = new HashMap<>();
}
