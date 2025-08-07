package rendering_api;

import org.lwjgl.glfw.GLFW;

public abstract class Input {

    public static final int IS_MOUSE_BUTTON = 0x80000000;
    public static final int BUTTON_MASK = 0x7FFFFFFF;

    protected Input() {
    }

    public static boolean isKeyPressed(int keycode) {
        if ((keycode & Input.IS_MOUSE_BUTTON) == 0)
            return GLFW.glfwGetKey(Window.getWindow(), keycode & Input.BUTTON_MASK) == GLFW.GLFW_PRESS;
        else
            return GLFW.glfwGetMouseButton(Window.getWindow(), keycode & Input.BUTTON_MASK) == GLFW.GLFW_PRESS;
    }

    public static char getChar(int key) {
        return switch (key) {
            case GLFW.GLFW_KEY_0, GLFW.GLFW_KEY_KP_0 -> '0';
            case GLFW.GLFW_KEY_1, GLFW.GLFW_KEY_KP_1 -> '1';
            case GLFW.GLFW_KEY_2, GLFW.GLFW_KEY_KP_2 -> '2';
            case GLFW.GLFW_KEY_3, GLFW.GLFW_KEY_KP_3 -> '3';
            case GLFW.GLFW_KEY_4, GLFW.GLFW_KEY_KP_4 -> '4';
            case GLFW.GLFW_KEY_5, GLFW.GLFW_KEY_KP_5 -> '5';
            case GLFW.GLFW_KEY_6, GLFW.GLFW_KEY_KP_6 -> '6';
            case GLFW.GLFW_KEY_7, GLFW.GLFW_KEY_KP_7 -> '7';
            case GLFW.GLFW_KEY_8, GLFW.GLFW_KEY_KP_8 -> '8';
            case GLFW.GLFW_KEY_9, GLFW.GLFW_KEY_KP_9 -> '9';

            case GLFW.GLFW_KEY_A -> 'A';
            case GLFW.GLFW_KEY_B -> 'B';
            case GLFW.GLFW_KEY_C -> 'C';
            case GLFW.GLFW_KEY_D -> 'D';
            case GLFW.GLFW_KEY_E -> 'E';
            case GLFW.GLFW_KEY_F -> 'F';
            case GLFW.GLFW_KEY_G -> 'G';
            case GLFW.GLFW_KEY_H -> 'H';
            case GLFW.GLFW_KEY_I -> 'I';
            case GLFW.GLFW_KEY_J -> 'J';
            case GLFW.GLFW_KEY_K -> 'K';
            case GLFW.GLFW_KEY_L -> 'L';
            case GLFW.GLFW_KEY_M -> 'M';
            case GLFW.GLFW_KEY_N -> 'N';
            case GLFW.GLFW_KEY_O -> 'O';
            case GLFW.GLFW_KEY_P -> 'P';
            case GLFW.GLFW_KEY_Q -> 'Q';
            case GLFW.GLFW_KEY_R -> 'R';
            case GLFW.GLFW_KEY_S -> 'S';
            case GLFW.GLFW_KEY_T -> 'T';
            case GLFW.GLFW_KEY_U -> 'U';
            case GLFW.GLFW_KEY_V -> 'V';
            case GLFW.GLFW_KEY_W -> 'W';
            case GLFW.GLFW_KEY_X -> 'X';
            case GLFW.GLFW_KEY_Y -> 'Z';    // Nobody will use this anyway to german keyboard layout it is
            case GLFW.GLFW_KEY_Z -> 'Y';    // Nobody will use this anyway to german keyboard layout it is

            case GLFW.GLFW_KEY_SPACE -> ' ';
            case GLFW.GLFW_KEY_MINUS -> '_';

            default -> 0;
        };
    }

    public abstract void setInputMode();

    public abstract void cursorPosCallback(long window, double xPos, double yPos);

    public abstract void mouseButtonCallback(long window, int button, int action, int mods);

    public abstract void scrollCallback(long window, double xScroll, double yScroll);

    public abstract void keyCallback(long window, int key, int scancode, int action, int mods);

    public abstract void charCallback(long window, int codePoint);
}
