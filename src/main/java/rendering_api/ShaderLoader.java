package rendering_api;

import java.util.HashMap;

public final class ShaderLoader {

    private ShaderLoader() {
    }

    public static GUIShader getGUIShader() {
        if (shaders.containsKey(GUI_SHADER)) return (GUIShader) shaders.get(GUI_SHADER);
        GUIShader shader = new GUIShader("assets/shaders/GUIVertex.glsl", "assets/shaders/GUIFragment.glsl");
        shaders.put(GUI_SHADER, shader);
        return shader;
    }


    public static void reloadShaders() {
        for (Shader shader : shaders.values()) shader.reload();
    }

    private static final int GUI_SHADER = 1;
    private static final HashMap<Integer, Shader> shaders = new HashMap<>();
}
