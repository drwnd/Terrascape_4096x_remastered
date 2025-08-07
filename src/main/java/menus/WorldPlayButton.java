package menus;

import org.joml.Vector2f;
import rendering_api.renderables.TextElement;
import rendering_api.renderables.UiButton;

import java.io.File;

public final class WorldPlayButton extends UiButton {

    public WorldPlayButton(Vector2f sizeToParent, Vector2f offsetToParent, File saveFile, MainMenu menu) {
        super(sizeToParent, offsetToParent, getAction(saveFile, menu));

        TextElement text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        text.setText(saveFile.getName());
        addRenderable(text);
    }

    private static Runnable getAction(File saveFile, MainMenu menu) {
        return () -> menu.setSelectedWorld(saveFile);
    }

    private static final Vector2f TEXT_SIZE = new Vector2f(0.008333334f, 0.022222223f);
}
