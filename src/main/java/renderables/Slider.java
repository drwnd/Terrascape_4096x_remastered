package renderables;

import menus.SettingsMenu;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import rendering_api.Window;
import settings.FloatSetting;

public final class Slider extends UiButton {

    public Slider(Vector2f sizeToParent, Vector2f offsetToParent, FloatSetting setting) {
        super(sizeToParent, offsetToParent);
        setAction(this::action);
        allowScaling(false);
        this.setting = setting;

        slider = new UiBackgroundElement(new Vector2f(0.05f, 1.0f), new Vector2f(0.0f, 0.0f));
        textElement = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE, setting.name());

        addRenderable(slider);
        addRenderable(textElement);

        matchSetting();
    }

    public void setToDefault() {
        setValue(setting.defaultValue());
    }

    public FloatSetting getSetting() {
        return setting;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
        textElement.setText("%s %s".formatted(setting.name(), value));
        slider.setOffsetToParent(new Vector2f(setting.fractionFromValue(value) - slider.getSizeToParent().x * 0.5f, 0.0f));
    }

    @Override
    public void dragOver(Vector2i pixelCoordinate) {
        action(pixelCoordinate, GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS);
    }

    private void matchSetting() {
        setValue(setting.value());
    }

    private void action(Vector2i cursorPos, int button, int action) {
        Vector2f position = Window.toPixelCoordinate(getPosition());
        Vector2f size = Window.toPixelSize(getSize());

        float fraction = (cursorPos.x - position.x) / size.x;
        fraction = Math.clamp(fraction, 0.0f, 1.0f);
        setValue(setting.valueFronFraction(fraction));

        if (getParent() instanceof SettingsMenu)
            ((SettingsMenu) getParent()).setSelectedSlider(this);
    }

    private final FloatSetting setting;
    private final UiBackgroundElement slider;
    private final TextElement textElement;
    private float value;

    private static final Vector2f TEXT_SIZE = new Vector2f(0.008333334f, 0.022222223f);
}
