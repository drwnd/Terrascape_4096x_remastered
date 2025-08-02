package main_menu;

import assets.TextureIdentifier;
import assets.AssetManager;
import assets.Texture;
import org.joml.Vector2f;
import rendering_api.GUIShader;
import rendering_api.ScreenElement;
import rendering_api.ShaderLoader;

public final class MainMenu extends ScreenElement {

    public MainMenu() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f), new MainMenuInput());
    }

    @Override
    protected void renderSelf(Vector2f offset) {
        GUIShader shader = ShaderLoader.getGUIShader();
        Texture background = AssetManager.getTexture(TextureIdentifier.ATLAS);
        shader.bind();
        shader.drawQuad(new Vector2f(-0.5f, -0.5f), new Vector2f(1.0f, 1.0f), background);
    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }
}
