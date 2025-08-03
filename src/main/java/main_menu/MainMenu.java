package main_menu;

import assets.identifiers.ShaderIdentifier;
import assets.identifiers.TextureIdentifier;
import assets.AssetManager;
import assets.Texture;
import org.joml.Vector2f;
import rendering_api.shaders.GuiShader;
import rendering_api.ScreenElement;

public final class MainMenu extends ScreenElement {

    public MainMenu() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f), new MainMenuInput());
    }

    @Override
    protected void renderSelf(Vector2f offset) {
        GuiShader shader = (GuiShader) AssetManager.getShader(ShaderIdentifier.GUI_BACKGROUND);
        Texture background = AssetManager.getTexture(TextureIdentifier.GUI_ELEMENT_BACKGROUND);
        shader.bind();
        shader.drawQuad(new Vector2f(-0.5f, -0.5f), new Vector2f(1.0f, 1.0f), background);
    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }
}
