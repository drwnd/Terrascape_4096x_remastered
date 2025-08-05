package rendering_api.shaders;

import assets.AssetManager;
import assets.identifiers.BufferIdentifier;
import assets.identifiers.ShaderIdentifier;
import assets.identifiers.TextureIdentifier;
import assets.identifiers.VertexArrayIdentifier;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL46;

import java.awt.*;
import java.nio.charset.StandardCharsets;

public class TextShader extends Shader {
    public static final int MAX_TEXT_LENGTH = 128;

    public TextShader(String vertexShaderFilePath, String fragmentShaderFilePath, ShaderIdentifier identifier) {
        super(vertexShaderFilePath, fragmentShaderFilePath, identifier);
    }

    @Override
    public void bind() {
        GL46.glUseProgram(programID);
        GL46.glDisable(GL46.GL_DEPTH_TEST);
        GL46.glDisable(GL46.GL_CULL_FACE);
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void drawText(Vector2f position, String text, Color color, boolean addTransparentBackground) {
        setUniform("string", toIntFormat(text));
        setUniform("position", position.x - 0.5f, position.y - 0.5f);
        setUniform("color", color);
        setUniform("textAtlas", 0);
        setUniform("addTransparentBackground", addTransparentBackground ? 1 : 0);

        GL46.glActiveTexture(0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, AssetManager.getTexture(TextureIdentifier.TEXT_ATLAS).getID());
        GL46.glBindVertexArray(AssetManager.getVertexArray(VertexArrayIdentifier.TEXT_ROW_VERTEX_ARRAY).getID());
        GL46.glEnableVertexAttribArray(0);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, AssetManager.getBuffer(BufferIdentifier.MODEL_INDEX_BUFFER).getID());

        GL46.glDrawElements(GL46.GL_TRIANGLES, 384, GL46.GL_UNSIGNED_INT, 0);
    }

    private static int[] toIntFormat(String text) {
        int[] array = new int[MAX_TEXT_LENGTH];
        byte[] stringBytes = text.getBytes(StandardCharsets.UTF_8);

        for (int index = 0, max = Math.min(text.length(), MAX_TEXT_LENGTH); index < max; index++)
            array[index] = stringBytes[index];

        return array;
    }
}
