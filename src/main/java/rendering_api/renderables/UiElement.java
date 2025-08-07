package rendering_api.renderables;

import assets.AssetManager;
import assets.Texture;
import assets.identifiers.ShaderIdentifier;
import assets.identifiers.TextureIdentifier;
import org.joml.Vector2f;
import rendering_api.shaders.GuiShader;

public class UiElement extends Renderable {
    public UiElement(Vector2f sizeToParent, Vector2f offsetToParent, TextureIdentifier texture) {
        super(sizeToParent, offsetToParent);
        this.texture = texture;
    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {
        GuiShader shader = (GuiShader) AssetManager.getShader(ShaderIdentifier.GUI);
        Texture background = AssetManager.getTexture(texture);
        shader.bind();
        shader.drawQuad(position, size, background);
    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }

    private final TextureIdentifier texture;
}
