package settings;

public final class Settings {

    public static void update(FloatSetting setting, float value) {
        setting.setValue(value);
    }

    public static void update(KeySetting setting, int value) {
        setting.setValue(value);
    }

    public static void update(ToggleSetting setting, boolean value) {
        setting.setValue(value);
    }


    public static void writeToFile() {

    }
}
