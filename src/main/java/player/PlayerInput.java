package player;

import menus.PauseMenu;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import rendering_api.Input;
import rendering_api.Window;
import server.GameHandler;
import settings.FloatSetting;
import settings.ToggleSetting;

public final class PlayerInput extends Input {
    @Override
    public void setInputMode() {
        GLFW.glfwSetInputMode(Window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        if (GLFW.glfwRawMouseMotionSupported()) {
            if (ToggleSetting.RAW_MOUSE_INPUT.value())
                GLFW.glfwSetInputMode(Window.getWindow(), GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_TRUE);
            else
                GLFW.glfwSetInputMode(Window.getWindow(), GLFW.GLFW_RAW_MOUSE_MOTION, GLFW.GLFW_FALSE);
        }
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

    public void rotateCamera() {
        float deltaX = cursorPos.x - lastCursorPos.x;
        float deltaY = cursorPos.y - lastCursorPos.y;

        float sensitivityFactor = FloatSetting.SENSITIVITY.value() * 0.6f + 0.2f;
        sensitivityFactor = 1.2f * sensitivityFactor * sensitivityFactor * sensitivityFactor;
        float rotationX = deltaX * sensitivityFactor;
        float rotationY = deltaY * sensitivityFactor;

        GameHandler.getPlayer().getCamera().moveRotation(rotationX, -rotationY);
        lastCursorPos.set(cursorPos);
    }

    private final Vector2i lastCursorPos = new Vector2i();
}
