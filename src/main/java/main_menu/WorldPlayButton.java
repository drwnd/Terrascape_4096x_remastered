package main_menu;

import assets.AssetManager;
import assets.Texture;
import assets.identifiers.ShaderIdentifier;
import assets.identifiers.TextureIdentifier;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rendering_api.ScreenElement;
import rendering_api.Window;
import rendering_api.shaders.GuiShader;
import rendering_api.shaders.TextShader;

import java.awt.*;
import java.io.File;

public final class WorldPlayButton extends ScreenElement {

    public WorldPlayButton(Vector2f sizeToParent, Vector2f offsetToParent, File saveFile) {
        super(sizeToParent, offsetToParent);
        this.saveFile = saveFile;
        this.name = saveFile.getName();
    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {
        GuiShader shader = (GuiShader) AssetManager.getShader(ShaderIdentifier.GUI_BACKGROUND);
        Texture background = AssetManager.getTexture(TextureIdentifier.GUI_ELEMENT_BACKGROUND);
        shader.bind();
        shader.setUniform("rimWidth", 10);
        shader.setUniform("screenSize", Window.getWidth(), Window.getHeight());
        shader.drawQuad(position, size, background);

        Vector2f center = new Vector2f(position).add(0.0f, size.y * 0.5f - Window.toRelativeY(TEXT_SIZE.y / 2));
        TextShader textShader = (TextShader) AssetManager.getShader(ShaderIdentifier.TEXT);
        textShader.bind();
        textShader.setUniform("screenSize", Window.getWidth(), Window.getHeight());
        textShader.setUniform("charSize", TEXT_SIZE);
        textShader.drawText(center, name, Color.WHITE, false);
    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }

    private final String name;
    private final File saveFile;

    private static final Vector2i TEXT_SIZE = new Vector2i(16, 24);
}
