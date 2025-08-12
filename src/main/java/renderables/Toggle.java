package renderables;

import assets.AssetManager;
import assets.Texture;
import assets.identifiers.ShaderIdentifier;
import assets.identifiers.TextureIdentifier;
import org.joml.Vector2f;
import rendering_api.shaders.GuiShader;
import settings.ToggleSetting;

public final class Toggle extends UiButton {

    public Toggle(Vector2f sizeToParent, Vector2f offsetToParent, ToggleSetting setting) {
        super(sizeToParent, offsetToParent);
        setAction(() -> value = !value);
        this.setting = setting;
        matchSetting();

        addRenderable(new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE, setting.name()));
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

    private boolean value;
    private final ToggleSetting setting;

    private static final Vector2f TEXT_SIZE = new Vector2f(0.008333334f, 0.022222223f);
}
