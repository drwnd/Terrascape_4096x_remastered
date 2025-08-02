package assets;

import org.lwjgl.opengl.GL46;
import rendering_api.ObjectLoader;

public class GuiElement extends Asset {

    public GuiElement(int vao, int vbo1, int vbo2, int vertexCount) {
        this.vao = vao;
        this.vbo1 = vbo1;
        this.vbo2 = vbo2;
        this.vertexCount = vertexCount;
    }

    public int getVao() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    @Override
    public void reload(String identifier) {
        GL46.glDeleteVertexArrays(vao);
        GL46.glDeleteBuffers(vbo1);
        GL46.glDeleteBuffers(vbo2);

        GuiElement guiElement = ObjectLoader.loadGuiElement(GuiElementIdentifier.valueOf(identifier));
        this.vao = guiElement.vao;
        this.vbo1 = guiElement.vbo1;
        this.vbo2 = guiElement.vbo2;
        this.vertexCount = guiElement.vertexCount;
    }

    private int vao, vertexCount;
    private int vbo1, vbo2;
}
