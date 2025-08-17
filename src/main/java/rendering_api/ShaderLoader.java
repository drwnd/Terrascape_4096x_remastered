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
            case OPAQUE_GEOMETRY -> new Shader("assets/shaders/Material.vert", "assets/shaders/OpaqueMaterial.frag", ShaderIdentifier.OPAQUE_GEOMETRY);
            case SKYBOX -> new Shader("assets/shaders/Skybox.vert", "assets/shaders/Skybox.frag", ShaderIdentifier.SKYBOX);
        };
    }


    private static Shader getGuiShader() {
        return new GuiShader("assets/shaders/Gui.vert", "assets/shaders/Gui.frag", ShaderIdentifier.GUI);
    }

    private static Shader getGuiBackgroundShader() {
        return new GuiBackgroundShader("assets/shaders/Gui.vert", "assets/shaders/GuiBackground.frag", ShaderIdentifier.GUI_BACKGROUND);
    }

    private static Shader getTextShader() {
        return new TextShader("assets/shaders/Text.vert", "assets/shaders/Text.frag", ShaderIdentifier.TEXT);
    }
}
