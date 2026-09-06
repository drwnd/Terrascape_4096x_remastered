package core.rendering_api;

import core.renderables.Renderable;

import static org.lwjgl.glfw.GLFW.*;

public class MenuInput<T extends Renderable> extends Input {

    public final ScrollCallback scrollCallback;
    public final MaxScrollGetter maxScrollGetter;
    protected T menu;

    public MenuInput(T menu) {
        super(menu);
        this.menu = menu;
        this.maxScrollGetter = () -> Float.POSITIVE_INFINITY;
        this.scrollCallback = (_) -> {
        };
    }

    public MenuInput(T menu, ScrollCallback scrollCallback, MaxScrollGetter maxScrollGetter) {
        super(menu);
        this.menu = menu;
        this.maxScrollGetter = maxScrollGetter == null ? () -> Float.POSITIVE_INFINITY : maxScrollGetter;
        this.scrollCallback = scrollCallback == null ? (_) -> {
        } : scrollCallback;
    }

    public float getScroll() {
        return scroll;
    }

    public void setScroll(float scroll) {
        float maxScroll = maxScrollGetter.getMaxScroll();
        scroll = maxScroll <= 0.0F ? 0.0F : Math.clamp(scroll, 0.0F, maxScroll);
        scrollCallback.scroll(scroll - this.scroll);
        this.scroll = scroll;
    }

    @Override
    public void setInputMode() {
        setStandardInputMode();
    }

    @Override
    public void cursorPosCallback(long window, double xPos, double yPos) {
        standardCursorPosCallBack(xPos, yPos);
        if (Input.isKeyPressed(GLFW_MOUSE_BUTTON_LEFT | IS_MOUSE_BUTTON) || Input.isKeyPressed(GLFW_MOUSE_BUTTON_RIGHT | IS_MOUSE_BUTTON))
            menu.dragOver(cursorPos);
        else menu.hoverOver(cursorPos);
    }

    @Override
    public void mouseButtonCallback(long window, int button, int action, int mods) {
        menu.clickOn(cursorPos, button, action);
    }

    @Override
    public void scrollCallback(long window, double xScroll, double yScroll) {
        float maxScroll = maxScrollGetter.getMaxScroll();
        float newScroll = maxScroll <= 0.0F ? 0.0F : Math.clamp((float) (scroll - yScroll * 0.05), 0.0F, maxScroll);
        scrollCallback.scroll(newScroll - scroll);
        scroll = newScroll;
        menu.hoverOver(cursorPos);
    }

    @Override
    public void keyCallback(long window, int key, int scancode, int action, int mods) {

    }

    @Override
    public void charCallback(long window, int codePoint) {

    }

    private float scroll = 0.0F;

    public interface ScrollCallback {
        void scroll(float scrolled);
    }

    public interface MaxScrollGetter {
        float getMaxScroll();
    }
}
