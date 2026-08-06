package core.settings.stand_alones;

import core.settings.KeySetting;
import core.settings.OptionSetting;
import core.settings.optionSettings.Option;

import static org.lwjgl.glfw.GLFW.*;

public final class StandAloneOptionSetting implements OptionSetting {

    public StandAloneOptionSetting(Option defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        nextKeySetting = new StandAloneKeySetting(GLFW_KEY_UNKNOWN);
        previousKeySetting = new StandAloneKeySetting(GLFW_KEY_UNKNOWN);
    }

    StandAloneOptionSetting(Option defaultValue, int defaultNextKeybind, int defaultPreviousKeybind) {
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
        throw new UnsupportedOperationException("Standalone Settings aren't Translatable");
    }

    @Override
    public int ordinal() {
        throw new UnsupportedOperationException("Standalone Settings aren't Translatable");
    }

    private Option value;
    private final Option defaultValue;

    private final StandAloneKeySetting nextKeySetting, previousKeySetting;
}
