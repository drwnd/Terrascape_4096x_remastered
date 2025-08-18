package assets;

import org.lwjgl.opengl.GL46;

public class GuiElement extends Asset {

    public GuiElement(int vao, int vertexCount) {
        this.vao = vao;
        this.vertexCount = vertexCount;
    }

    public int getVao() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public void delete() {
        GL46.glDeleteVertexArrays(vao);
    }

    private final int vao, vertexCount;
}
