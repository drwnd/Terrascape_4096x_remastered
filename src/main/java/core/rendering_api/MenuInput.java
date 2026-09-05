package core.rendering_api;

import core.renderables.Renderable;

public class MenuInput<T extends Renderable> extends Input {

    public MenuInput(T menu) {
        super(menu);
        this.menu = menu;
        this.scrollCallback = null;
    }

    public MenuInput(T menu, ScrollCallback scrollCallback) {
        super(menu);
        this.menu = menu;
        this.scrollCallback = scrollCallback;
    }

    protected T menu;

    @Override
    public void setInputMode() {
        setStandardInputMode();
    }

    @Override
    public void cursorPosCallback(long window, double xPos, double yPos) {
        standardCursorPosCallBack(xPos, yPos);
        menu.hoverOver(cursorPos);
    }

    @Override
    public void mouseButtonCallback(long window, int button, int action, int mods) {
        menu.clickOn(cursorPos, button, action);
    }

    @Override
    public void scrollCallback(long window, double xScroll, double yScroll) {
        float newScroll = Math.max((float) (scroll - yScroll * 0.05), 0.0F);
        if (scrollCallback != null) scrollCallback.scroll(newScroll - scroll);
        scroll = newScroll;

        menu.hoverOver(cursorPos); // Fixes buttons being selected even if the cursor isn't hovered over them
    }

    @Override
    public void keyCallback(long window, int key, int scancode, int action, int mods) {

    }

    @Override
    public void charCallback(long window, int codePoint) {

    }

    private float scroll = 0.0F;
    private final ScrollCallback scrollCallback;

    public interface ScrollCallback {
        void scroll(float scrolled);
    }
}
