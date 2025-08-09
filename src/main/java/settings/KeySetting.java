package settings;

import org.lwjgl.glfw.GLFW;
import rendering_api.Input;

public enum KeySetting {

    MOVE_FORWARD(GLFW.GLFW_KEY_W),
    MOVE_BACK(GLFW.GLFW_KEY_S),
    MOVE_RIGHT(GLFW.GLFW_KEY_D),
    MOVE_LEFT(GLFW.GLFW_KEY_A),
    JUMP(GLFW.GLFW_KEY_SPACE),
    SPRINT(GLFW.GLFW_KEY_LEFT_CONTROL),
    SNEAK(GLFW.GLFW_KEY_LEFT_SHIFT),
    CRAWL(GLFW.GLFW_KEY_CAPS_LOCK),
    FLY_FAST(GLFW.GLFW_KEY_TAB),
    HOT_BAR_SLOT_1(GLFW.GLFW_KEY_1),
    HOT_BAR_SLOT_2(GLFW.GLFW_KEY_2),
    HOT_BAR_SLOT_3(GLFW.GLFW_KEY_3),
    HOT_BAR_SLOT_4(GLFW.GLFW_KEY_4),
    HOT_BAR_SLOT_5(GLFW.GLFW_KEY_5),
    HOT_BAR_SLOT_6(GLFW.GLFW_KEY_6),
    HOT_BAR_SLOT_7(GLFW.GLFW_KEY_7),
    HOT_BAR_SLOT_8(GLFW.GLFW_KEY_8),
    HOT_BAR_SLOT_9(GLFW.GLFW_KEY_9),
    DESTROY(GLFW.GLFW_MOUSE_BUTTON_LEFT | Input.IS_MOUSE_BUTTON),
    USE(GLFW.GLFW_MOUSE_BUTTON_RIGHT | Input.IS_MOUSE_BUTTON),
    PICK_BLOCK(GLFW.GLFW_MOUSE_BUTTON_MIDDLE | Input.IS_MOUSE_BUTTON),
    INVENTORY(GLFW.GLFW_KEY_E),
    DEBUG_MENU(GLFW.GLFW_KEY_F3),
    NO_CLIP(GLFW.GLFW_KEY_P),
    ZOOM(GLFW.GLFW_KEY_V),
    INCREASE_BREAK_PLACE_SIZE(GLFW.GLFW_KEY_UP),
    DECREASE_BREAK_PLACE_SIZE(GLFW.GLFW_KEY_DOWN),
    DROP(GLFW.GLFW_KEY_Q),
    RESIZE_WINDOW(GLFW.GLFW_KEY_F11),
    RELOAD_ASSETS(GLFW.GLFW_KEY_I),
    RELOAD_SETTINGS(GLFW.GLFW_KEY_O);

    public static void setIfPresent(String name, String value) {
        try {
            valueOf(name).setValue(Integer.parseInt(value));
        } catch (IllegalArgumentException ignore) {

        }
    }

    KeySetting(int defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    void setValue(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public int defaultValue() {
        return defaultValue;
    }

    private final int defaultValue;
    private int value;
}
