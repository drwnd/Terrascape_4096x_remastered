package assets;

public enum TextureIdentifier {

    ATLAS("assets/textures/atlas256.png");

    TextureIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    private final String identifier;
}
