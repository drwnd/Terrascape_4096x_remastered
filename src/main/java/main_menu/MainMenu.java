package main_menu;

import assets.identifiers.ShaderIdentifier;
import assets.identifiers.TextureIdentifier;
import assets.AssetManager;
import assets.Texture;
import org.joml.Vector2f;
import rendering_api.Window;
import rendering_api.shaders.GuiShader;
import rendering_api.ScreenElement;

import java.awt.*;
import java.io.File;

public final class MainMenu extends ScreenElement {

    public MainMenu() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f));
        Window.setInput(new MainMenuInput(this));

        File[] savedWorlds = getSavedWorlds();
        for (int index = 0; index < savedWorlds.length; index++) {
            File saveFile = savedWorlds[index];
            WorldPlayButton button = new WorldPlayButton(new Vector2f(0.6f, 0.1f), new Vector2f(0.2f, 1.0f - 0.15f * (index + 1)), saveFile);
            addRenderable(button);
        }
    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {
        GuiShader shader = (GuiShader) AssetManager.getShader(ShaderIdentifier.GUI_BACKGROUND);
        Texture background = AssetManager.getTexture(TextureIdentifier.GUI_ELEMENT_BACKGROUND);
        shader.bind();
        shader.setUniform("rimWidth", 30);
        shader.setUniform("screenSize", Window.getWidth(), Window.getHeight());
        shader.drawQuad(new Vector2f(0.0f, 0.0f), new Vector2f(1.0f, 1.0f), background);
    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }

    private static File[] getSavedWorlds() {
        File savesFile = new File("saves");
        if (!savesFile.exists()) {
            savesFile.mkdir();
            return new File[0];
        }
        File[] savedWorlds = savesFile.listFiles();
        return savedWorlds == null ? new File[0] : savedWorlds;
    }
}
