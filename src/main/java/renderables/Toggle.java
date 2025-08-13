package renderables;

import assets.AssetManager;
import assets.Texture;
import assets.identifiers.ShaderIdentifier;
import assets.identifiers.TextureIdentifier;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import rendering_api.shaders.GuiShader;
import settings.ToggleSetting;

public final class Toggle extends UiButton {

    public Toggle(Vector2f sizeToParent, Vector2f offsetToParent, ToggleSetting setting) {
        super(sizeToParent, offsetToParent);
        setAction(getAction());
        this.setting = setting;
        matchSetting();

        addRenderable(new TextElement(new Vector2f(0.05f, 0.5f), setting.name()));
    }

    public void setToDefault() {
        value = setting.defaultValue();
    }

    public ToggleSetting getSetting() {
        return setting;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public void renderSelf(Vector2f position, Vector2f size) {
        super.renderSelf(position, size);

        position = new Vector2f(position).add(0.785f * size.x, 0.15f * size.y);
        size = new Vector2f(size).mul(0.2f, 0.7f);

        GuiShader shader = (GuiShader) AssetManager.getShader(ShaderIdentifier.GUI);
        Texture texture = AssetManager.getTexture(value ? TextureIdentifier.TOGGLE_ACTIVATED : TextureIdentifier.TOGGLE_DEACTIVATED);
        shader.bind();
        shader.drawQuad(position, size, texture);
    }

    private void matchSetting() {
        value = setting.value();
    }

    private Clickable getAction() {
        return (Vector2i cursorPos, int button, int action) -> {
            if (action == GLFW.GLFW_PRESS) value = !value;
        };
    }

    private boolean value;
    private final ToggleSetting setting;
}
