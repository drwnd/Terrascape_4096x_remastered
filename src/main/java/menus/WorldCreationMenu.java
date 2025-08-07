package menus;

import org.joml.Vector2f;
import org.joml.Vector2i;
import rendering_api.Window;
import rendering_api.renderables.TextElement;
import rendering_api.renderables.UiBackgroundElement;
import rendering_api.renderables.UiButton;

public class WorldCreationMenu extends UiBackgroundElement {

    public WorldCreationMenu() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f));

        Vector2f sizeToParent = new Vector2f(0.25f, 0.1f);

        UiButton backButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.85f), getBackButtonRunnable());
        TextElement text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        text.setText("Back");
        backButton.addRenderable(text);

        addRenderable(backButton);
    }

    @Override
    public void setOnTop() {
        Window.setInput(new WorldCreationMenuInput(this));
    }

    private static Runnable getBackButtonRunnable() {
        return Window::removeTopRenderable;
    }

    private static final Vector2i TEXT_SIZE = new Vector2i(16, 24);
}
