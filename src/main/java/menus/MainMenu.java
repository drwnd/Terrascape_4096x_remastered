package menus;

import org.joml.Vector2f;
import org.joml.Vector2i;
import rendering_api.Window;
import rendering_api.renderables.TextElement;
import rendering_api.renderables.UiBackgroundElement;
import rendering_api.renderables.Renderable;
import rendering_api.renderables.UiButton;

import java.io.File;
import java.util.ArrayList;

public final class MainMenu extends UiBackgroundElement {

    public MainMenu() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f));

        File[] savedWorlds = getSavedWorlds();
        Vector2f sizeToParent = new Vector2f(0.6f, 0.1f);
        for (int index = 0; index < savedWorlds.length; index++) {
            File saveFile = savedWorlds[index];

            Vector2f offsetToParent = new Vector2f(0.35f, 1.0f - 0.15f * (index + 1));
            WorldPlayButton button = new WorldPlayButton(sizeToParent, offsetToParent, saveFile, this);

            addRenderable(button);
            worldButtons.add(button);
        }

        sizeToParent = new Vector2f(0.25f, 0.1f);

        UiButton settingsButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.85f), getSettingsRunnable());
        TextElement text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        text.setText("Settings");
        settingsButton.addRenderable(text);

        UiButton createNewWorldButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.7f), getCreateWorldRunnable());
        text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        text.setText("New World");
        createNewWorldButton.addRenderable(text);

        UiButton closeApplicationButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.55f), getCloseApplicationRunnable());
        text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        text.setText("Quit Game");
        closeApplicationButton.addRenderable(text);

        playWorldButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.4f), null);
        text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        playWorldButton.addRenderable(text);
        playWorldButton.setVisible(false);

        deleteWorldButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.05f), null);
        text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        deleteWorldButton.addRenderable(text);
        deleteWorldButton.setVisible(false);

        addRenderable(settingsButton);
        addRenderable(createNewWorldButton);
        addRenderable(closeApplicationButton);
        addRenderable(playWorldButton);
        addRenderable(deleteWorldButton);
    }

    public void moveWorldButtons(float movement) {
        Vector2f offset = new Vector2f(0, movement);
        for (Renderable element : worldButtons) element.move(offset);
    }

    public void setSelectedWorld(File saveFile) {
        playWorldButton.setAction(getPlayWorldRunnable(saveFile));
        deleteWorldButton.setAction(getDeleteWorldRunnable(saveFile));

        ((TextElement) playWorldButton.getChildren().getFirst()).setText("Play %s".formatted(saveFile.getName()));
        ((TextElement) deleteWorldButton.getChildren().getFirst()).setText("Delete %s".formatted(saveFile.getName()));

        playWorldButton.setVisible(true);
        deleteWorldButton.setVisible(true);
    }

    @Override
    public void setOnTop() {
        Window.setInput(new MainMenuInput(this));
    }

    @Override
    public void clickOn(Vector2i pixelCoordinate) {
        boolean buttonFound = false;
        for (Renderable button : getChildren())
            if (button.isVisible() && button instanceof UiButton && button.containsPixelCoordinate(pixelCoordinate)) {
                ((UiButton) button).run();
                buttonFound = true;
            }

        if (!buttonFound) {
            playWorldButton.setVisible(false);
            deleteWorldButton.setVisible(false);
        }
    }

    private static File[] getSavedWorlds() {
        File savesFile = new File("saves");
        if (!savesFile.exists()) savesFile.mkdir();
        return savesFile.listFiles();
    }

    private static Runnable getSettingsRunnable() {
        return () -> System.out.println("Settings is not implemented jet. :(");
    }

    private static Runnable getCreateWorldRunnable() {
        return () -> Window.setTopRenderable(new WorldCreationMenu());
    }

    private static Runnable getPlayWorldRunnable(File saveFile) {
        return () -> System.out.printf("Playing %s is not implemented jet. :(%n", saveFile.getName());
    }

    private static Runnable getDeleteWorldRunnable(File saveFile) {
        return () -> System.out.printf("Deleting %s is not implemented jet. :(%n", saveFile.getName());
    }

    private static Runnable getCloseApplicationRunnable() {
        return Window::removeTopRenderable;
    }

    private final ArrayList<WorldPlayButton> worldButtons = new ArrayList<>();
    private final UiButton playWorldButton, deleteWorldButton;

    private static final Vector2f TEXT_SIZE = new Vector2f(0.008333334f, 0.022222223f);
}
