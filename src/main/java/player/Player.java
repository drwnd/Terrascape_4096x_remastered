package player;

import rendering_api.Window;

public final class Player {

    public static void init() {
        renderer = new Renderer();
        Window.setTopRenderable(renderer);
        setInput();
    }

    public static void setInput() {
        Window.setInput(new PlayerInput());
    }

    private static Renderer renderer;
}
