package renderables;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import rendering_api.Window;
import settings.FloatSetting;
import settings.KeySetting;
import settings.Settings;
import settings.ToggleSetting;

import java.util.ArrayList;

public final class SettingsRenderable extends UiBackgroundElement {

    public SettingsRenderable() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f));
        input = new SettingsRenderableInput(this);
        Vector2f sizeToParent = new Vector2f(0.1f, 0.1f);

        UiButton backButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.85f), getBackButtonAction());
        TextElement text = new TextElement(new Vector2f(0.15f, 0.5f), "Back");
        backButton.addRenderable(text);

        UiButton applyChangesButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.7f), getApplyChangesButtonAction());
        text = new TextElement(new Vector2f(0.15f, 0.5f), "Apply");
        applyChangesButton.addRenderable(text);

        UiButton resetButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.55f), getResetSettingsButtonAction());
        text = new TextElement(new Vector2f(0.15f, 0.5f), "Reset All");
        resetButton.addRenderable(text);

        addRenderable(backButton);
        addRenderable(applyChangesButton);
        addRenderable(resetButton);
    }

    public void scrollSettingButtons(float scroll) {
        Vector2f offset = new Vector2f(0, scroll);

        for (Slider slider : sliders) slider.move(offset);
        for (KeySelector keySelector : keySelectors) keySelector.move(offset);
        for (Toggle toggle : toggles) toggle.move(offset);
        for (UiButton resetButton : resetButtons) resetButton.move(offset);
    }

    public void setSelectedSlider(Slider slider) {
        this.selectedSlider = slider;
    }

    public void cancelSelection() {
        if (selectedSlider != null) {
            selectedSlider = null;
            return;
        }
        Window.removeTopRenderable();
    }

    public void addSlider(FloatSetting setting) {
        settingsCount++;
        Vector2f sizeToParent = new Vector2f(0.6f, 0.1f);
        Vector2f offsetToParent = new Vector2f(0.35f, 1.0f - 0.15f * settingsCount + input.getScroll());

        Slider slider = new Slider(sizeToParent, offsetToParent, setting);
        addRenderable(slider);
        sliders.add(slider);

        createResetButton(settingsCount).setAction((Vector2i cursorPos, int button, int action) -> {
            if (action == GLFW.GLFW_PRESS) slider.setToDefault();
        });
    }

    public void addKeySelector(KeySetting setting) {
        settingsCount++;
        Vector2f sizeToParent = new Vector2f(0.6f, 0.1f);
        Vector2f offsetToParent = new Vector2f(0.35f, 1.0f - 0.15f * settingsCount + input.getScroll());

        KeySelector keySelector = new KeySelector(sizeToParent, offsetToParent, setting);
        addRenderable(keySelector);
        keySelectors.add(keySelector);

        createResetButton(settingsCount).setAction((Vector2i cursorPos, int button, int action) -> {
            if (action == GLFW.GLFW_PRESS) keySelector.setToDefault();
        });
    }

    public void addToggle(ToggleSetting setting) {
        settingsCount++;
        Vector2f sizeToParent = new Vector2f(0.6f, 0.1f);
        Vector2f offsetToParent = new Vector2f(0.35f, 1.0f - 0.15f * settingsCount + input.getScroll());

        Toggle toggle = new Toggle(sizeToParent, offsetToParent, setting);
        addRenderable(toggle);
        toggles.add(toggle);

        createResetButton(settingsCount).setAction((Vector2i cursorPos, int button, int action) -> {
            if (action == GLFW.GLFW_PRESS) toggle.setToDefault();
        });
    }

    private UiButton createResetButton(int counter) {
        Vector2f sizeToParent = new Vector2f(0.1f, 0.1f);
        Vector2f offsetToParent = new Vector2f(0.225f, 1.0f - 0.15f * counter + input.getScroll());
        UiButton resetButton = new UiButton(sizeToParent, offsetToParent);

        TextElement text = new TextElement(new Vector2f(0.15f, 0.5f), "Reset");
        resetButton.addRenderable(text);

        addRenderable(resetButton);
        resetButtons.add(resetButton);
        return resetButton;
    }

    @Override
    public void setOnTop() {
        float scroll = input == null ? 0.0f : input.getScroll();
        input = new SettingsRenderableInput(this);
        input.setScroll(scroll);
        Window.setInput(input);
        selectedSlider = null;
    }

    @Override
    public void dragOver(Vector2i pixelCoordinate) {
        if (selectedSlider != null)
            selectedSlider.dragOver(pixelCoordinate);
        else super.dragOver(pixelCoordinate);
    }

    @Override
    public void clickOn(Vector2i pixelCoordinate, int mouseButton, int action) {
        boolean buttonFound = false;
        for (Renderable renderable : getChildren())
            if (renderable.isVisible() && renderable.containsPixelCoordinate(pixelCoordinate)) {
                renderable.clickOn(pixelCoordinate, mouseButton, action);
                buttonFound = true;
                break;
            }

        if (!buttonFound) selectedSlider = null;
    }

    private Clickable getApplyChangesButtonAction() {
        return (Vector2i pixelCoordinate, int button, int action) -> {
            if (action != GLFW.GLFW_PRESS) return;
            for (Slider slider : sliders) Settings.update(slider.getSetting(), slider.getValue());
            for (KeySelector keySelector : keySelectors) Settings.update(keySelector.getSetting(), keySelector.getValue());
            for (Toggle toggle : toggles) Settings.update(toggle.getSetting(), toggle.getValue());
            Settings.writeToFile();
            Window.removeTopRenderable();
        };
    }

    private Clickable getResetSettingsButtonAction() {
        return (Vector2i pixelCoordinate, int button, int action) -> {
            if (action != GLFW.GLFW_PRESS) return;
            for (UiButton resetButton : resetButtons) resetButton.clickOn(pixelCoordinate, button, action);
        };
    }

    private Clickable getBackButtonAction() {
        return (Vector2i pixelCoordinate, int button, int action) -> {
            if (action != GLFW.GLFW_PRESS) return;

            for (Slider slider : sliders) slider.matchSetting();
            for (KeySelector keySelector : keySelectors) keySelector.matchSetting();
            for (Toggle toggle : toggles) toggle.matchSetting();

            Window.removeTopRenderable();
        };
    }

    private int settingsCount = 0;
    private Slider selectedSlider;
    private SettingsRenderableInput input;

    private final ArrayList<Slider> sliders = new ArrayList<>();
    private final ArrayList<KeySelector> keySelectors = new ArrayList<>();
    private final ArrayList<Toggle> toggles = new ArrayList<>();
    private final ArrayList<UiButton> resetButtons = new ArrayList<>();
}
