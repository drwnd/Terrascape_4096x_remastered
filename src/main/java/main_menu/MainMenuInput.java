package main_menu;

import org.lwjgl.glfw.GLFW;
import rendering_api.Input;

public final class MainMenuInput extends Input {
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
    }
}
