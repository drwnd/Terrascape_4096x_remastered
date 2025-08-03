package main_menu;

import assets.identifiers.ShaderIdentifier;
import assets.identifiers.TextureIdentifier;
import assets.AssetManager;
import assets.Texture;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rendering_api.Window;
import rendering_api.shaders.GuiShader;
import rendering_api.ScreenElement;
import rendering_api.shaders.TextShader;

import java.awt.*;

public final class MainMenu extends ScreenElement {

    public MainMenu() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f), new MainMenuInput());
    }

    @Override
    protected void renderSelf(Vector2f offset) {
        GuiShader guiShader = (GuiShader) AssetManager.getShader(ShaderIdentifier.GUI_BACKGROUND);
        Texture background = AssetManager.getTexture(TextureIdentifier.GUI_ELEMENT_BACKGROUND);
        guiShader.bind();
        guiShader.drawQuad(new Vector2f(-0.5f, -0.5f), new Vector2f(1.0f, 1.0f), background);

        TextShader textShader = (TextShader) AssetManager.getShader(ShaderIdentifier.TEXT);
        textShader.bind();
        textShader.setUniform("screenSize", Window.getWidth(), Window.getHeight());
        textShader.setUniform("charSize", new Vector2i(16, 24));
        textShader.drawText(new Vector2f(0.0f, 0.0f), "This is sample text!", Color.WHITE, false);
    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }
}
