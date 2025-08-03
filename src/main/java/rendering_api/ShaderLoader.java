package rendering_api;

import assets.identifiers.ShaderIdentifier;
import rendering_api.shaders.GuiBackgroundShader;
import rendering_api.shaders.GuiShader;
import rendering_api.shaders.Shader;

public final class ShaderLoader {

    private ShaderLoader() {
    }


    public static Shader loadShader(ShaderIdentifier identifier) {
        return switch (identifier) {
            case GUI -> getGuiShader();
            case GUI_BACKGROUND -> getGuiBackgroundShader();
        };
    }


    private static Shader getGuiShader() {
        return new GuiShader("assets/shaders/Gui.vert", "assets/shaders/Gui.frag");
    }

    private static Shader getGuiBackgroundShader() {
        return new GuiBackgroundShader("assets/shaders/Gui.vert", "assets/shaders/GuiBackground.frag");
    }
}
