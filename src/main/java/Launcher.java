import main_menu.MainMenu;
import rendering_api.WindowManager;

public class Launcher {

    public static void main(String[] args) {
        WindowManager window = new WindowManager("", 1000, 1000, true, false);
        MainMenu menu = new MainMenu();
        window.setScreenElement(menu);
        window.renderLoop();
    }
}
