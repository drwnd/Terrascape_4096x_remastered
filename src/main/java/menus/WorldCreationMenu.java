package menus;

import org.joml.Vector2f;
import renderables.TextElement;
import renderables.TextField;
import renderables.UiBackgroundElement;
import renderables.UiButton;
import rendering_api.Window;

import java.io.File;

public final class WorldCreationMenu extends UiBackgroundElement {

    public WorldCreationMenu() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f));

        Vector2f sizeToParent = new Vector2f(0.6f, 0.1f);
        TextField nameField = new TextField(sizeToParent, new Vector2f(0.35f, 0.85f), "World Name");
        TextField seedField = new TextField(sizeToParent, new Vector2f(0.35f, 0.7f), "World Seed");

        sizeToParent = new Vector2f(0.25f, 0.1f);
        UiButton backButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.85f), getBackButtonRunnable());
        TextElement text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), "Back");
        backButton.addRenderable(text);

        UiButton createButton = new UiButton(sizeToParent, new Vector2f(0.05f, 0.7f), getCreateButtonRunnable(nameField, seedField));
        text = new TextElement(new Vector2f(1.0f, 1.0f), new Vector2f(0.05f, 0.5f), "Create");
        createButton.addRenderable(text);

        addRenderable(backButton);
        addRenderable(createButton);
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

    private static Runnable getCreateButtonRunnable(TextField nameField, TextField seedField) {
        return () -> {
            if (nameField.getText().isEmpty()) return;
            File[] savedWorlds = MainMenu.getSavedWorlds();
            for (File file : savedWorlds) if (file.getName().equals(nameField.getText())) return;

            long seed = getSeed(seedField.getText());

            File newWorldFile = new File("saves/%s".formatted(nameField.getText()));
            newWorldFile.mkdir();
            Window.removeTopRenderable();
        };
    }

    private static long getSeed(String seedString) {
        if (seedString.isEmpty()) return getRandomSeed();

        try {
            return Long.parseLong(seedString);
        } catch (NumberFormatException ignore) {

        }
        long[] longs = toLongArray(seedString.toCharArray());
        long seed = 0;
        for (long aLong : longs) seed ^= aLong;
        return seed;
    }

    private static long getRandomSeed() {
        long seed = 0;
        for (int bitCounter = 0; bitCounter < 64; bitCounter++) {
            seed <<= 1;
            seed |= Math.random() > 0.5 ? 1 : 0;
        }
        return seed;
    }

    private static long[] toLongArray(char[] charArray) {
        long[] longs = new long[charArray.length / 8 + 1];
        for (int index = 0; index < longs.length; index++) {
            long current = 0;

            for(int charIndex = index * 8; charIndex < charArray.length && charIndex < (index + 1) * 8; charIndex++) {
                current <<= 8;
                current |= charArray[charIndex] & 0xFFL;
            }

            longs[index] = current;
        }
        return longs;
    }
}
