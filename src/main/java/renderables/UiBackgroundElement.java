package renderables;

import assets.AssetManager;
import assets.Texture;
import assets.identifiers.ShaderIdentifier;
import assets.identifiers.TextureIdentifier;
import org.joml.Vector2f;
import rendering_api.Window;
import rendering_api.shaders.GuiShader;
import settings.FloatSetting;

public class UiBackgroundElement extends Renderable {
    public UiBackgroundElement(Vector2f sizeToParent, Vector2f offsetToParent) {
        super(sizeToParent, offsetToParent);
    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {
        GuiShader shader = (GuiShader) AssetManager.getShader(ShaderIdentifier.GUI_BACKGROUND);
        Texture background = AssetManager.getTexture(TextureIdentifier.GUI_ELEMENT_BACKGROUND);
        shader.bind();
        shader.setUniform("rimWidth", RIM_WIDTH * FloatSetting.GUI_SIZE.value());
        shader.setUniform("aspectRatio", (float) Window.getWidth() / Window.getHeight());
        shader.drawQuad(position, size, background);
    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }

    private static final float RIM_WIDTH = 0.015625f;
}
