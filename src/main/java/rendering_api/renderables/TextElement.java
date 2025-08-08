package rendering_api.renderables;

import assets.AssetManager;
import assets.identifiers.ShaderIdentifier;
import org.joml.Vector2f;
import rendering_api.Window;
import rendering_api.shaders.TextShader;

import java.awt.*;

public class TextElement extends Renderable {
    public TextElement(Vector2f sizeToParent, Vector2f offsetToParent, Vector2f charSize) {
        super(sizeToParent, offsetToParent);
        this.charSize = charSize;
    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {
        float distanceToParentBorder = getParent().getPosition().x + getParent().getSize().x - position.x;
        int maxLengthInsideParent = (int) (distanceToParentBorder / charSize.x);
        maxLengthInsideParent = Math.min(text.length(), maxLengthInsideParent);

        position = new Vector2f(position.x, position.y - charSize.y * 0.5f);
        TextShader textShader = (TextShader) AssetManager.getShader(ShaderIdentifier.TEXT);
        textShader.bind();
        textShader.setUniform("screenSize", Window.getWidth(), Window.getHeight());
        textShader.setUniform("charSize", (int) (Window.getWidth() * charSize.x), (int) (Window.getHeight() * charSize.y));
        textShader.drawText(position, text.substring(0, maxLengthInsideParent), Color.WHITE, hasTransparentBackground);
    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }

    public void setText(String text) {
        this.text = text;
    }

    public void setHasTransparentBackground(boolean hasTransparentBackground) {
        this.hasTransparentBackground = hasTransparentBackground;
    }

    private String text = "";
    private boolean hasTransparentBackground = false;
    private final Vector2f charSize;
}
