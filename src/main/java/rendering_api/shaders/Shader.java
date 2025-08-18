package rendering_api.shaders;

import assets.Asset;
import assets.identifiers.ShaderIdentifier;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class Shader extends Asset {

    public Shader(String vertexShaderFilePath, String fragmentShaderFilePath, ShaderIdentifier identifier) {
        uniforms = new HashMap<>();

        try {
            String vertexShaderCode = loadShaderCode(vertexShaderFilePath);
            String fragmentShaderCode = loadShaderCode(fragmentShaderFilePath);

            programID = createProgram();
            int vertexShaderID = createVertexShader(vertexShaderCode, programID);
            int fragmentShaderID = createFragmentShader(fragmentShaderCode, programID);
            link(programID, vertexShaderID, fragmentShaderID);

            System.out.printf("Creating uniforms for Shader %s%n", identifier);
            createUniforms(vertexShaderCode);
            createUniforms(fragmentShaderCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void bind() {
        GL46.glUseProgram(programID);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL46.glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformName, int[] data) {
        GL46.glUniform1iv(uniforms.get(uniformName), data);
    }

    public void setUniform(String uniformName, int x, int y) {
        GL46.glUniform2i(uniforms.get(uniformName), x, y);
    }

    public void setUniform(String uniformName, Vector2i value) {
        GL46.glUniform2i(uniforms.get(uniformName), value.x, value.y);
    }

    public void setUniform(String uniformName, int x, int y, int z) {
        GL46.glUniform3i(uniforms.get(uniformName), x, y, z);
    }

    public void setUniform(String uniformName, int x, int y, int z, int w) {
        GL46.glUniform4i(uniforms.get(uniformName), x, y, z, w);
    }

    public void setUniform(String uniformName, float x, float y, float z) {
        GL46.glUniform3f(uniforms.get(uniformName), x, y, z);
    }

    public void setUniform(String uniformName, float value) {
        GL46.glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector2f value) {
        GL46.glUniform2f(uniforms.get(uniformName), value.x, value.y);
    }

    public void setUniform(String uniformName, float x, float y) {
        GL46.glUniform2f(uniforms.get(uniformName), x, y);
    }

    public void setUniform(String uniformName, Vector3f value) {
        GL46.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, int value) {
        GL46.glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, boolean value) {
        GL46.glUniform1i(uniforms.get(uniformName), value ? 1 : 0);
    }

    public void setUniform(String uniformName, Color color) {
        GL46.glUniform3f(uniforms.get(uniformName), color.getRed() * 0.003921569f, color.getGreen() * 0.003921569f, color.getBlue() * 0.003921569f);
    }

    @Override
    public void delete() {
        uniforms.clear();
        if (programID != 0) GL46.glDeleteProgram(programID);
    }


    private static int createProgram() throws Exception {
        int programID = GL46.glCreateProgram();
        if (programID == 0) throw new Exception("Could not create Shader");
        return programID;
    }

    private static int createVertexShader(String shaderCode, int programID) throws Exception {
        return createShader(shaderCode, GL46.GL_VERTEX_SHADER, programID);
    }

    private static int createFragmentShader(String shaderCode, int programID) throws Exception {
        return createShader(shaderCode, GL46.GL_FRAGMENT_SHADER, programID);
    }

    private static void link(int programID, int vertexShaderID, int fragmentShaderID) throws Exception {
        GL46.glLinkProgram(programID);

        if (GL46.glGetProgrami(programID, GL46.GL_LINK_STATUS) == 0)
            throw new Exception("Error linking shader code: " + GL46.glGetProgramInfoLog(programID, 1024));

        if (vertexShaderID != 0) GL46.glDetachShader(programID, vertexShaderID);
        if (fragmentShaderID != 0) GL46.glDetachShader(programID, fragmentShaderID);

        GL46.glValidateProgram(programID);
        if (GL46.glGetProgrami(programID, GL46.GL_VALIDATE_STATUS) == 0)
            throw new Exception("Unable to validate shader code: " + GL46.glGetProgramInfoLog(programID, 1024));
    }

    private static int createShader(String shaderCode, int shaderType, int programID) throws Exception {
        int shaderID = GL46.glCreateShader(shaderType);
        if (shaderID == 0) throw new Exception("Error creating shader. Type: " + shaderType);

        GL46.glShaderSource(shaderID, shaderCode);
        GL46.glCompileShader(shaderID);

        if (GL46.glGetShaderi(shaderID, GL46.GL_COMPILE_STATUS) == 0)
            throw new Exception("Error compiling shader code: Type: " + shaderType + "Info: " + GL46.glGetShaderInfoLog(shaderID, 1024));

        GL46.glAttachShader(programID, shaderID);

        return shaderID;
    }

    private static String loadShaderCode(String filePath) throws FileNotFoundException {
        String result;

        InputStream in = new FileInputStream(filePath);
        Scanner scanner = new Scanner(in, StandardCharsets.UTF_8);
        result = scanner.useDelimiter("\\A").next();

        return result;
    }

    private void createUniforms(String shaderCode) {
        String[] lines = shaderCode.split("\n");

        for (String line : lines) {
            String stripped = strip(line);
            if (!stripped.startsWith("uniform")) continue;

            String uniformName = stripped.split(" ")[2];
            createUniform(uniformName);
        }
    }

    private void createUniform(String uniformName) {
        int uniformLocation = GL46.glGetUniformLocation(programID, uniformName);
        if (uniformLocation == -1) System.err.println("Could not find uniform " + uniformName);
        uniforms.put(uniformName, uniformLocation);
        System.out.printf("-Created uniform %s with binding %s%n", uniformName, uniformLocation);
    }

    private static String strip(String line) {
        return line.strip()                                  // Remove accidental white space
                .replaceAll(";", "")        // Remove trailing semicolon
                .replaceAll("\\[.*]", "")   // Remove Array declarations (C-Style ones would cause problems)
                .replaceAll(" +", " ");     // Remove unnecessary white space
    }

    protected final int programID;
    private final HashMap<String, Integer> uniforms;
}
