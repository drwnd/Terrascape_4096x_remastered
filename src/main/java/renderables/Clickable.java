package renderables;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

public interface Clickable extends Runnable {

    void clickOn(Vector2i cursorPos, int button, int action);

    default void run() {
         clickOn(new Vector2i(), GLFW.GLFW_MOUSE_BUTTON_LEFT, GLFW.GLFW_PRESS);
    }
}
