package core.settings;

import core.settings.optionSettings.*;
import core.language.Language;
import core.settings.stand_alones.StandAloneKeySetting;

import static org.lwjgl.glfw.GLFW.*;

public enum CoreOptionSettings implements OptionSetting {
    FONT(new FontOption("Default")),
    LANGUAGE(new Language("English")),
    LOG_MESSAGES(LogMessages.NONE);

    CoreOptionSettings(Option defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        nextKeySetting = new StandAloneKeySetting(GLFW_KEY_UNKNOWN);
        previousKeySetting = new StandAloneKeySetting(GLFW_KEY_UNKNOWN);
    }

    CoreOptionSettings(Option defaultValue, int defaultNextKeybind, int defaultPreviousKeybind) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        nextKeySetting = new StandAloneKeySetting(defaultNextKeybind);
        previousKeySetting = new StandAloneKeySetting(defaultPreviousKeybind);
    }

    @Override
    public void setValue(Option value) {
        this.value = value;
    }

    @Override
    public Option value() {
        return value;
    }

    @Override
    public Option defaultValue() {
        return defaultValue;
    }

    @Override
    public KeySetting nextKeySetting() {
        return nextKeySetting;
    }

    @Override
    public KeySetting previousKeySetting() {
        return previousKeySetting;
    }


    @Override
    public String translationFileName() {
        return "coreOptionSettings";
    }

    private Option value;
    private final Option defaultValue;

    private final StandAloneKeySetting nextKeySetting, previousKeySetting;
}
