package main_menu;

import org.joml.Vector2f;
import org.joml.Vector2i;
import rendering_api.screen_elements.TextElement;
import rendering_api.screen_elements.UiButton;

import java.awt.*;
import java.io.File;

public final class WorldPlayButton extends UiButton {

    public WorldPlayButton(Vector2f sizeToParent, Vector2f offsetToParent, File saveFile) {
        super(sizeToParent, offsetToParent, getAction(saveFile));

        TextElement text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        text.setText(saveFile.getName());
        addRenderable(text);
    }

    private static Runnable getAction(File saveFile) {
        return () -> System.out.println(saveFile.getName());
    }

    private static final Vector2i TEXT_SIZE = new Vector2i(16, 24);
}
