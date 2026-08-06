package game.settings;

import core.settings.KeySetting;
import core.settings.OptionSetting;
import core.settings.optionSettings.Option;

import core.settings.stand_alones.StandAloneKeySetting;
import game.player.interaction.PlaceMode;
import game.player.rendering.Camera;
import game.player.rendering.RenderingOptimizer;

import static org.lwjgl.glfw.GLFW.*;

public enum OptionSettings implements OptionSetting {
    OCCLUSION_CULLING(RenderingOptimizer.OcclusionCullingOptions.NORMAL),
    PERSPECTIVE(Camera.Perspective.FIRST_PERSON, GLFW_KEY_UNKNOWN, GLFW_KEY_C),
    PLACE_MODE(PlaceMode.REPLACE);

    OptionSettings(Option defaultValue) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        nextKeySetting = new StandAloneKeySetting(GLFW_KEY_UNKNOWN);
        previousKeySetting = new StandAloneKeySetting(GLFW_KEY_UNKNOWN);
    }

    OptionSettings(Option defaultValue, int defaultNextKeybind, int defaultPreviousKeybind) {
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


    private Option value;
    private final Option defaultValue;

    private final StandAloneKeySetting nextKeySetting, previousKeySetting;

    @Override
    public String translationFileName() {
        return "optionSettings";
    }
}
