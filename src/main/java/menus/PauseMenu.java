package menus;

import assets.identifiers.TextureIdentifier;
import org.joml.Vector2f;
import player.Player;
import renderables.TextElement;
import renderables.UiButton;
import renderables.UiElement;
import rendering_api.Window;
import server.World;

public final class PauseMenu extends UiElement {
    public PauseMenu() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f), TextureIdentifier.INVENTORY_OVERLAY);

        Vector2f sizeToParent = new Vector2f(0.6f, 0.1f);

        UiButton quitButton = new UiButton(sizeToParent, new Vector2f(0.2f, 0.3f), getQuitButtonAction());
        quitButton.addRenderable(new TextElement(new Vector2f(0.05f, 0.5f), "Quit"));

        UiButton settingsButton = new UiButton(sizeToParent, new Vector2f(0.2f, 0.45f),getSettingsButtonAction());
        settingsButton.addRenderable(new TextElement(new Vector2f(0.05f, 0.5f), "Settings"));

        UiButton playButton = new UiButton(sizeToParent, new Vector2f(0.2f, 0.6f), getPlayButtonAction());
        playButton.addRenderable(new TextElement(new Vector2f(0.05f, 0.5f), "Play"));

        addRenderable(quitButton);
        addRenderable(settingsButton);
        addRenderable(playButton);
    }

    @Override
    public void setOnTop() {
        Window.setInput(new PauseMenuInput(this));
        World.pauseTicks();
    }

    private static Runnable getQuitButtonAction() {
        return () -> {
            Window.removeTopRenderable();
            World.cleanUp();
            Window.removeTopRenderable();
        };
    }

    private static Runnable getSettingsButtonAction() {
        return () -> Window.setTopRenderable(new SettingsMenu());
    }

    private static Runnable getPlayButtonAction() {
        return () -> {
            Window.removeTopRenderable();
            World.startTicks();
            Player.setInput();
        };
    }
}
