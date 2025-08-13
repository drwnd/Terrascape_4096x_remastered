package player;

import menus.PauseMenu;
import org.lwjgl.glfw.GLFW;
import rendering_api.Input;
import rendering_api.Window;

public final class PlayerInput extends Input {
    @Override
    public void setInputMode() {
        GLFW.glfwSetInputMode(Window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    @Override
    public void cursorPosCallback(long window, double xPos, double yPos) {
        standardCursorPosCallBack(xPos, yPos);
    }

    @Override
    public void mouseButtonCallback(long window, int button, int action, int mods) {

    }

    @Override
    public void scrollCallback(long window, double xScroll, double yScroll) {

    }

    @Override
    public void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) Window.setTopRenderable(new PauseMenu());
    }

    @Override
    public void charCallback(long window, int codePoint) {

    }
}
