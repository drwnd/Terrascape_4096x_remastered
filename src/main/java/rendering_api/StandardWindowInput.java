package rendering_api;

import assets.AssetManager;
import org.lwjgl.glfw.GLFW;

public final class StandardWindowInput extends Input {

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
        if (key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_PRESS) Window.toggleFullScreen();
        if (key == GLFW.GLFW_KEY_I && action == GLFW.GLFW_PRESS) AssetManager.reload();
    }

    @Override
    public void charCallback(long window, int codePoint) {

    }
}
