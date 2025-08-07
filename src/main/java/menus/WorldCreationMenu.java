package menus;

import org.joml.Vector2f;
import rendering_api.Window;
import rendering_api.renderables.TextElement;
import rendering_api.renderables.TextField;
import rendering_api.renderables.UiBackgroundElement;
import rendering_api.renderables.UiButton;

public final class WorldCreationMenu extends UiBackgroundElement {

    public WorldCreationMenu() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f));

        Vector2f sizeToParent = new Vector2f(0.25f, 0.1f);
        UiButton backButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.85f), getBackButtonRunnable());
        TextElement text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        text.setText("Back");
        backButton.addRenderable(text);

        sizeToParent = new Vector2f(0.6f, 0.1f);
        TextField nameField = new TextField(sizeToParent, new Vector2f(0.35f, 0.85f), "World Name");
        TextField seedField = new TextField(sizeToParent, new Vector2f(0.35f, 0.7f), "World Seed");

        addRenderable(backButton);
        addRenderable(nameField);
        addRenderable(seedField);
    }

    @Override
    public void setOnTop() {
        Window.setInput(new WorldCreationMenuInput(this));
    }

    private static Runnable getBackButtonRunnable() {
        return Window::removeTopRenderable;
    }

    private static final Vector2f TEXT_SIZE = new Vector2f(0.008333334f, 0.022222223f);
}
