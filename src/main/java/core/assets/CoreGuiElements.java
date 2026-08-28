package core.assets;

import core.assets.identifiers.GuiElementIdentifier;

public enum CoreGuiElements implements GuiElementIdentifier {

    QUAD(new float[]{0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0}, new float[]{0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1});


    CoreGuiElements(float[] vertices, float[] textureCoordinates) {
        this.vertices = vertices;
        this.textureCoordinates = textureCoordinates;
    }

    @Override
    public Object[] attributes() {
        return new Object[]{vertices, textureCoordinates};
    }

    @Override
    public int[] attributeSizes() {
        return new int[]{2, 2};
    }

    private final float[] vertices, textureCoordinates;
}
