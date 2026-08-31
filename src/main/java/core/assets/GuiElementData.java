package core.assets;

public record GuiElementData(float[][] floatAttributes, int[][] intAttributes, int[] attributeSizes) {

    public int getVertexCount() {
        if (floatAttributes.length != 0) return floatAttributes[0].length / attributeSizes[0];
        else return intAttributes[0].length / attributeSizes[0];
    }
}
