package assets.identifiers;

import utils.Constants;

public enum GuiElementIdentifier {

    QUAD(Constants.QUAD_VERTICES, Constants.QUAD_TEXTURE_COORDINATES);

    GuiElementIdentifier(float[] vertices, float[] textureCoordinates) {
        this.vertices = vertices;
        this.textureCoordinates = textureCoordinates;
    }

    public String getIdentifier() {
        return name() + "_GUI_ELEMENT";
    }

    public final float[] vertices, textureCoordinates;
}
