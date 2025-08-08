package menus;

import org.joml.Vector2f;
import rendering_api.Window;
import renderables.TextElement;
import renderables.UiBackgroundElement;
import renderables.UiButton;

public class SettingsMenu extends UiBackgroundElement {

    public SettingsMenu() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f));
        Vector2f sizeToParent = new Vector2f(0.25f, 0.1f);

        UiButton closeApplicationButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.85f), Window::removeTopRenderable);
        TextElement text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        text.setText("Cancel");
        closeApplicationButton.addRenderable(text);

        UiButton applyChangesButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.7f), getApplyChangesButtonRunnable());
        text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        text.setText("Apply");
        applyChangesButton.addRenderable(text);

        UiButton resetButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.55f), getResetSettingsButtonRunnable());
        text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), TEXT_SIZE);
        text.setText("Reset");
        resetButton.addRenderable(text);

        addRenderable(closeApplicationButton);
        addRenderable(applyChangesButton);
        addRenderable(resetButton);
    }

    @Override
    public void setOnTop() {
        Window.setInput(new SettingsMenuInput(this));
    }

    private static Runnable getApplyChangesButtonRunnable() {
        return () -> System.out.println("This isn't implemented jet. :(");
    }

    private static Runnable getResetSettingsButtonRunnable() {
        return () -> System.out.println("This isn't implemented jet. :(");
    }

    private static final Vector2f TEXT_SIZE = new Vector2f(0.008333334f, 0.022222223f);
}
