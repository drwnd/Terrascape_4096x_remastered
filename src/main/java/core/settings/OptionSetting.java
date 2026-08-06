package core.settings;

import core.settings.optionSettings.Option;

public interface OptionSetting extends Setting {

    default boolean setIfPresent(String name, String value) {
        if (!name().equalsIgnoreCase(name)) return false;

        String[] values = value.split("#");

        Option savedValue = defaultValue().value(values[0]);
        if (savedValue == null) return false;
        setValue(savedValue);

        if (values.length >= 2) nextKeySetting().setKeybind(Integer.parseInt(values[1]));
        if (values.length >= 3) previousKeySetting().setKeybind(Integer.parseInt(values[2]));
        return true;
    }

    default String toSaveValue() {
        return "%s#%d#%d".formatted(String.valueOf(value()), nextKeySetting().keybind(), previousKeySetting().keybind());
    }

    void setValue(Option value);

    Option value();

    Option defaultValue();

    KeySetting nextKeySetting();

    KeySetting previousKeySetting();
}
