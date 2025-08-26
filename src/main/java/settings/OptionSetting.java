package settings;

import settings.optionSettings.Option;
import settings.optionSettings.Visibility;

public enum OptionSetting {

    FPS_VISIBILITY(Visibility.WHEN_SCREEN_OPEN),
    POSITION_VISIBILITY(Visibility.WHEN_SCREEN_OPEN),
    CHUNK_POSITION_VISIBILITY(Visibility.WHEN_SCREEN_OPEN),
    DIRECTION_VISIBILITY(Visibility.WHEN_SCREEN_OPEN),
    ROTATION_VISIBILITY(Visibility.WHEN_SCREEN_OPEN),
    SEED_VISIBILITY(Visibility.WHEN_SCREEN_OPEN),
    CHUNK_IS_NULL_VISIBILITY(Visibility.WHEN_SCREEN_OPEN),
    CHUNK_STATUS_VISIBILITY(Visibility.WHEN_SCREEN_OPEN),
    CHUNK_IDENTIFIERS_VISIBILITY(Visibility.WHEN_SCREEN_OPEN);

    public static void setIfPresent(String name, String value) {
        try {
            OptionSetting setting = valueOf(name);

            Option<?> savedValue = setting.defaultValue.value(value);

            if (savedValue != null) setting.setValue(savedValue);
        } catch (IllegalArgumentException ignore) {

        }
    }

    OptionSetting(Option<?> defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    void setValue(Option<?> value) {
        this.value = value;
    }

    public Option<?> value() {
        return value;
    }

    public Option<?> defaultValue() {
        return defaultValue;
    }

    private Option<?> value;
    private final Option<?> defaultValue;
}
