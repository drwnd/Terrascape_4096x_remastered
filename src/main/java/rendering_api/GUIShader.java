package rendering_api;

import assets.AssetManager;
import assets.GuiElement;
import assets.GuiElementIdentifier;
import assets.Texture;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL46;

public class GUIShader extends Shader {
    public GUIShader(String vertexShaderFilePath, String fragmentShaderFilePath) {
        super(vertexShaderFilePath, fragmentShaderFilePath);
    }

    @Override
    public void bind() {
        GL46.glUseProgram(programID);
        GL46.glDisable(GL46.GL_DEPTH_TEST);
        GL46.glDisable(GL46.GL_CULL_FACE);
        GL46.glDisable(GL46.GL_BLEND);
    }

    public void drawQuad(Vector2f position, Vector2f size, Texture texture) {
        GuiElement quad = AssetManager.getGuiElement(GuiElementIdentifier.QUAD);

        GL46.glBindVertexArray(quad.getVao());
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);

        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, texture.getTextureID());

        setUniform("textureAtlas", 0);
        setUniform("position", position);
        setUniform("size", size);

        GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, quad.getVertexCount());
    }
}
