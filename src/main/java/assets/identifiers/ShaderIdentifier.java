package assets.identifiers;

public enum ShaderIdentifier {

    GUI, GUI_BACKGROUND, TEXT, OPAQUE_GEOMETRY, SKYBOX;

    public String getIdentifier() {
        return name() + "_SHADER";
    }
}
