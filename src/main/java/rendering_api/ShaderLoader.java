package rendering_api;

import assets.identifiers.ShaderIdentifier;
import rendering_api.shaders.GuiBackgroundShader;
import rendering_api.shaders.GuiShader;
import rendering_api.shaders.Shader;
import rendering_api.shaders.TextShader;

public final class ShaderLoader {

    private ShaderLoader() {
    }


    public static Shader loadShader(ShaderIdentifier identifier) {
        return switch (identifier) {
            case GUI -> getGuiShader();
            case GUI_BACKGROUND -> getGuiBackgroundShader();
            case TEXT -> getTextShader();
        };
    }


    private static Shader getGuiShader() {
        return new GuiShader("assets/shaders/Gui.vert", "assets/shaders/Gui.frag");
    }

    private static Shader getGuiBackgroundShader() {
        return new GuiBackgroundShader("assets/shaders/Gui.vert", "assets/shaders/GuiBackground.frag");
    }

    private static Shader getTextShader() {
        return new TextShader("assets/shaders/Text.vert", "assets/shaders/Text.frag");
    }
}
