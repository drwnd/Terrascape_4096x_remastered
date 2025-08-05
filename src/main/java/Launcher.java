import main_menu.MainMenu;
import rendering_api.Window;

public class Launcher {

    public static void main(String[] args) {
        Window.init("Terrascape * 4096 remastered", 1000, 1000, true, true);
        MainMenu menu = new MainMenu();
        Window.setScreenElement(menu);
        Window.renderLoop();
        Window.cleanUp();
    }
}
