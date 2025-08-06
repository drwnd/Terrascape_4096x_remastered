package main_menu;

import assets.AssetManager;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import rendering_api.Input;
import rendering_api.Window;

public final class MainMenuInput extends Input {
    public MainMenuInput(MainMenu menu) {
        super();
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
        float newScroll = Math.max((float) (scroll - yScroll * 0.05), 0.0f);
        menu.moveWorldButtons(newScroll - scroll);
        scroll = newScroll;
    }

    @Override
    public void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_PRESS) Window.toggleFullScreen();
        if (key == GLFW.GLFW_KEY_I && action == GLFW.GLFW_PRESS) AssetManager.reload();
    }

    @Override
    public void charCallback(long window, int codePoint) {

    }

    private final MainMenu menu;
    private float scroll = 0;
    private final Vector2i cursorPos = new Vector2i();
}
