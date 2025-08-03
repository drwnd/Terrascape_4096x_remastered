package rendering_api;

import assets.GuiElement;
import assets.identifiers.GuiElementIdentifier;
import assets.Texture;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import rendering_api.shaders.TextShader;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class ObjectLoader {

    public static Texture loadTexture(String filepath) {
        int width, height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filepath, w, h, c, 4);
            if (buffer == null) throw new RuntimeException("Image FIle " + filepath + " not loaded " + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();
        }

        int id = GL46.glGenTextures();
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, id);
        GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width, height, 0, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, buffer);
        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
        STBImage.stbi_image_free(buffer);
        return new Texture(id);
    }

    public static GuiElement loadGuiElement(GuiElementIdentifier identifier) {
        int vao = createVAO();
        int vbo1 = storeDateInAttributeList(0, 2, identifier.vertices);
        int vbo2 = storeDateInAttributeList(1, 2, identifier.textureCoordinates);

        GL46.glBindVertexArray(0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo1);
        GL46.glDeleteBuffers(vbo1);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo2);
        GL46.glDeleteBuffers(vbo2);

        return new GuiElement(vao, identifier.vertices.length);
    }

    public static int generateModelIndexBuffer() {
        int[] indices = new int[393216];
        int index = 0;
        for (int i = 0; i < indices.length; i += 6) {
            indices[i] = index;
            indices[i + 1] = index + 1;
            indices[i + 2] = index + 2;
            indices[i + 3] = index + 3;
            indices[i + 4] = index + 2;
            indices[i + 5] = index + 1;
            index += 4;
        }
        int id = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, id);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, indices, GL46.GL_STATIC_DRAW);

        return id;
    }

    public static int generateTextRowVertexArray() {
        int vao = createVAO();

        int[] textData = new int[TextShader.MAX_TEXT_LENGTH * 4];
        for (int i = 0; i < textData.length; i += 4) {
            textData[i] = i >> 2;
            textData[i + 1] = i >> 2 | 128;
            textData[i + 2] = i >> 2 | 256;
            textData[i + 3] = i >> 2 | 384;
        }
        int vbo = storeDateInAttributeList(textData);

        GL46.glBindVertexArray(0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
        GL46.glDeleteBuffers(vbo);

        return vao;
    }


    private static int createVAO() {
        int vao = GL46.glGenVertexArrays();
        GL46.glBindVertexArray(vao);
        return vao;
    }

    private static int storeDateInAttributeList(int attributeNo, int size, float[] data) {
        int vbo = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, data, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(attributeNo, size, GL46.GL_FLOAT, false, 0, 0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        return vbo;
    }

    private static int storeDateInAttributeList(int[] data) {
        int vbo = GL46.glGenBuffers();
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, data, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribIPointer(0, 1, GL46.GL_INT, 0, 0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
        return vbo;
    }
}
