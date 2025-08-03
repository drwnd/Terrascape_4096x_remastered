package assets;

import assets.identifiers.GuiElementIdentifier;
import org.lwjgl.opengl.GL46;
import rendering_api.ObjectLoader;

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
    public void reload(String identifier) {
        System.out.printf("Reloading GuiElement %s%n", identifier);
        GL46.glDeleteVertexArrays(vao);

        GuiElement guiElement = ObjectLoader.loadGuiElement(GuiElementIdentifier.valueOf(identifier));
        this.vao = guiElement.vao;
        this.vertexCount = guiElement.vertexCount;
    }

    private int vao, vertexCount;
}
