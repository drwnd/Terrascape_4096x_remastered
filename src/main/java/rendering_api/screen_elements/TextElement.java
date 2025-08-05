package rendering_api.screen_elements;

import assets.AssetManager;
import assets.identifiers.ShaderIdentifier;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rendering_api.Window;
import rendering_api.shaders.TextShader;

import java.awt.*;

public class TextElement extends ScreenElement {
    public TextElement(Vector2f sizeToParent, Vector2f offsetToParent, Vector2i charSize) {
        super(sizeToParent, offsetToParent);
        this.charSize = charSize;
    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {
        position = new Vector2f(position.x, position.y - Window.toRelativeY(charSize.y / 2));
        TextShader textShader = (TextShader) AssetManager.getShader(ShaderIdentifier.TEXT);
        textShader.bind();
        textShader.setUniform("screenSize", Window.getWidth(), Window.getHeight());
        textShader.setUniform("charSize", charSize);
        textShader.drawText(position, text, Color.WHITE, hasTransparentBackground);
    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCharSize(Vector2i charSize) {
        this.charSize = charSize;
    }

    public void setHasTransparentBackground(boolean hasTransparentBackground) {
        this.hasTransparentBackground = hasTransparentBackground;
    }

    private String text = "";
    private Vector2i charSize;
    private boolean hasTransparentBackground = false;
}
