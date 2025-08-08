package rendering_api.renderables;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import rendering_api.Input;
import rendering_api.Window;

public final class TextFieldInput extends Input {

    public TextFieldInput(TextField field) {
        this.field = field;
    }

    @Override
    public void setInputMode() {

    }

    @Override
    public void cursorPosCallback(long window, double xPos, double yPos) {
        cursorPos.set((int) xPos, Window.getHeight() - (int) yPos);
    }

    @Override
    public void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action != GLFW.GLFW_PRESS) return;

        if (!field.containsPixelCoordinate(cursorPos)) {
            field.getParent().setOnTop();
            field.getParent().hoverOver(cursorPos);
        }
    }

    @Override
    public void scrollCallback(long window, double xScroll, double yScroll) {

    }

    @Override
    public void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action != GLFW.GLFW_PRESS && action != GLFW.GLFW_REPEAT) return;
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            field.getParent().setOnTop();
            field.getParent().hoverOver(cursorPos);
        }
        if (key == GLFW.GLFW_KEY_BACKSPACE) handleBackspace();
    }

    @Override
    public void charCallback(long window, int codePoint) {
        char[] chars = Character.toChars(codePoint);
        field.setText(field.getText() + toString(chars));
    }

    private void handleBackspace() {
        String currentText = field.getText();
        if (currentText.isEmpty()) return;

        if (Input.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL) || Input.isKeyPressed(GLFW.GLFW_KEY_RIGHT_CONTROL)) {
            int spaceIndex = currentText.lastIndexOf(' ');
            if (spaceIndex == -1) field.setText("");
            else field.setText(currentText.substring(0, spaceIndex));
        } else field.setText(currentText.substring(0, currentText.length() - 1));
    }

    private String toString(char[] chars) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : chars) stringBuilder.append(c);
        return stringBuilder.toString();
    }

    private final TextField field;
    private final Vector2i cursorPos = new Vector2i();
}
