package menus;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import rendering_api.Input;
import rendering_api.Window;

public class WorldCreationMenuInput extends Input {

    public WorldCreationMenuInput(WorldCreationMenu menu) {
        this.menu = menu;
    }

    @Override
    public void setInputMode() {

    }

    @Override
    public void cursorPosCallback(long window, double xPos, double yPos) {
        cursorPos.set((int) xPos, Window.getHeight() - (int) yPos);

        menu.hoverOver(cursorPos);
    }

    @Override
    public void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action != GLFW.GLFW_PRESS) return;

        menu.clickOn(cursorPos);
    }

    @Override
    public void scrollCallback(long window, double xScroll, double yScroll) {

    }

    @Override
    public void keyCallback(long window, int key, int scancode, int action, int mods) {

    }

    @Override
    public void charCallback(long window, int codePoint) {

    }

    private final WorldCreationMenu menu;
    private final Vector2i cursorPos = new Vector2i();
}
