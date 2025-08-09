package settings;

public enum ToggleSetting {
    SCROLL_HOT_BAR(true),
    RAW_MOUSE_INPUT(true),
    DO_SHADOW_MAPPING(false),
    X_RAY(false);

    ToggleSetting(boolean defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    void setValue(boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }

    public boolean defaultValue() {
        return defaultValue;
    }

    private final boolean defaultValue;
    private boolean value;
}
