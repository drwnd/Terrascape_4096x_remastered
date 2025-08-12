package menus;

import org.lwjgl.glfw.GLFW;
import rendering_api.Input;

public class SettingsMenuInput extends Input {

    public SettingsMenuInput(SettingsMenu menu) {
        super(menu);
        this.menu = menu;
    }

    public float getScroll() {
        return scroll;
    }

    public void setScroll(float scroll) {
        this.scroll = scroll;
    }

    @Override
    public void setInputMode() {
        setStandardInputMode();
    }

    @Override
    public void cursorPosCallback(long window, double xPos, double yPos) {
        standardCursorPosCallBack(xPos, yPos);
        if (Input.isKeyPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT | IS_MOUSE_BUTTON))
            menu.dragOver(cursorPos);
        else menu.hoverOver(cursorPos);
    }

    @Override
    public void mouseButtonCallback(long window, int button, int action, int mods) {
        menu.clickOn(cursorPos, button, action);
    }

    @Override
    public void scrollCallback(long window, double xScroll, double yScroll) {
        float newScroll = Math.max((float) (scroll - yScroll * 0.05), 0.0f);
        menu.scrollSettingButtons(newScroll - scroll);
        scroll = newScroll;

        menu.hoverOver(cursorPos); // Fixes buttons being selected even if the cursor isn't hovered over them
    }

    @Override
    public void keyCallback(long window, int key, int scancode, int action, int mods) {

    }

    @Override
    public void charCallback(long window, int codePoint) {

    }

    private final SettingsMenu menu;
    private float scroll = 0;
}
