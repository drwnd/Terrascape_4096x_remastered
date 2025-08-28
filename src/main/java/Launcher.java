import menus.MainMenu;
import rendering_api.Window;
import settings.Settings;

public final class Launcher {

    public static void main(String[] args) {
        Settings.loadFromFile();
        Window.init("Terrascape * 4096 remastered");
        Window.pushRenderable(new MainMenu());
        Window.renderLoop();
        Window.cleanUp();
    }
}
