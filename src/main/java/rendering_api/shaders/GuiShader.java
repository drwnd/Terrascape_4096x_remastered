package rendering_api.shaders;

import assets.AssetManager;
import assets.GuiElement;
import assets.identifiers.GuiElementIdentifier;
import assets.Texture;
import assets.identifiers.ShaderIdentifier;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL46;

public class GuiShader extends Shader {
    public GuiShader(String vertexShaderFilePath, String fragmentShaderFilePath, ShaderIdentifier identifier) {
        super(vertexShaderFilePath, fragmentShaderFilePath, identifier);
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
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, texture.getID());

        setUniform("image", 0);
        setUniform("position", position.x - 0.5f, position.y - 0.5f);
        setUniform("size", size);

        GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, quad.getVertexCount());
    }
}
