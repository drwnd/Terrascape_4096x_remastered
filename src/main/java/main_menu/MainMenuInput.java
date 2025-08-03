package main_menu;

import assets.AssetManager;
import org.lwjgl.glfw.GLFW;
import rendering_api.Input;
import rendering_api.Window;

public final class MainMenuInput extends Input {
    public MainMenuInput() {
        super();
    }

    @Override
    public void setInputMode() {

    }

    @Override
    public void cursorPosCallback(long window, double xPos, double yPos) {

    }

    @Override
    public void mouseButtonCallback(long window, int button, int action, int mods) {

    }

    @Override
    public void scrollCallback(long window, double xScroll, double yScroll) {

    }

    @Override
    public void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW.GLFW_KEY_ESCAPE) GLFW.glfwSetWindowShouldClose(window, true);
        if (key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_PRESS) Window.toggleFullScreen();
        if (key == GLFW.GLFW_KEY_I && action == GLFW.GLFW_PRESS) AssetManager.reload();
    }
}
