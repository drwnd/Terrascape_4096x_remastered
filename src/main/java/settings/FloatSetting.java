package settings;

public enum FloatSetting {
    FOV(10.0f, 175.0f, 90.0f),
    GUI_SIZE(0.1f, 10.0f, 1.0f),
    SENSITIVITY(0.0f, 1.0f, 0.14612676056338028f),
    REACH(0.0f, 500.0f, 5.0f),
    TEXT_SIZE(0.0f, 10.0f, 1.0f),
    MASTER_AUDIO(0.0f, 10.0f, 0.5f),
    FOOTSTEPS_AUDIO(0.0f, 10.0f, 1.0f),
    PLACE_AUDIO(0.0f, 10.0f, 1.0f),
    DIG_AUDIO(0.0f, 10.0f, 1.0f),
    INVENTORY_AUDIO(0.0f, 10.0f, 1.0f),
    MISCELLANEOUS_AUDIO(0.0f, 10.0f, 1.0f);

    FloatSetting(float min, float max, float defaultValue) {
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    void setValue(float value) {
        this.value = value;
    }

    public float value() {
        return value;
    }

    public float valueFronFraction(float fraction) {
        return min + fraction * (max - min);
    }

    public float fractionFromValue(float value) {
        return (value - min) / (max - min);
    }

    public float defaultValue() {
        return defaultValue;
    }

    private final float min, max, defaultValue;
    private float value;
}
