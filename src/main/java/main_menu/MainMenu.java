package main_menu;

import org.joml.Vector2f;
import org.joml.Vector2i;
import rendering_api.Window;
import rendering_api.screen_elements.UiBackgroundElement;
import rendering_api.screen_elements.ScreenElement;
import rendering_api.screen_elements.UiButton;

import java.io.File;
import java.util.ArrayList;

public final class MainMenu extends UiBackgroundElement {

    public MainMenu() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f));
        Window.setInput(new MainMenuInput(this));

        File[] savedWorlds = getSavedWorlds();
        for (int index = 0; index < savedWorlds.length; index++) {
            File saveFile = savedWorlds[index];
            WorldPlayButton button = new WorldPlayButton(new Vector2f(0.6f, 0.1f), new Vector2f(0.2f, 1.0f - 0.15f * (index + 1)), saveFile);
            addRenderable(button);
            worldButtons.add(button);
        }
    }

    public void moveWorldButtons(float movement) {
        Vector2f offset = new Vector2f(0, movement);
        for (ScreenElement element : worldButtons) element.move(offset);
    }

    public void clickOn(Vector2i pixelCoordinate) {
        for (ScreenElement button : getChildren())
            if (button instanceof UiButton && button.containsPixelCoordinate(pixelCoordinate))
                ((UiButton) button).run();
    }

    public void hoverOver(Vector2i pixelCoordinate) {
        for (ScreenElement button : getChildren()) button.setFocused(button.containsPixelCoordinate(pixelCoordinate));
    }

    private static File[] getSavedWorlds() {
        File savesFile = new File("saves");
        if (!savesFile.exists()) savesFile.mkdir();
        return savesFile.listFiles();
    }

    private final ArrayList<WorldPlayButton> worldButtons = new ArrayList<>();
}
