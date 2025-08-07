import menus.MainMenu;
import rendering_api.Window;

public final class Launcher {

    public static void main(String[] args) {
        Window.init("Terrascape * 4096 remastered", 1000, 1000, true, true);
        MainMenu menu = new MainMenu();
        Window.setTopRenderable(menu);
        Window.renderLoop();
        Window.cleanUp();
    }
}
