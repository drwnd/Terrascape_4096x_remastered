package rendering_api;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;
import renderables.Renderable;
import settings.FloatSetting;

import java.util.ArrayList;


public final class Window {

    private Window() {
    }

    public static void init(String title) {
        Window.maximized = true;

        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        createWindow(title);
        GL.createCapabilities();

        GL46.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        GL46.glClearColor(0, 0, 0, 1);
        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glDepthFunc(GL46.GL_LESS);
        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glCullFace(GL46.GL_BACK);
    }

    private static void createWindow(String title) {
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL46.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL46.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL46.GL_TRUE);

        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if (vidMode == null) throw new RuntimeException("Could not get video mode");

        if (maximized) {
            window = GLFW.glfwCreateWindow(vidMode.width(), vidMode.height(), title, GLFW.glfwGetPrimaryMonitor(), MemoryUtil.NULL);
            width = vidMode.width();
            height = vidMode.height();
        } else {
            width = vidMode.width() / 2;
            height = vidMode.height() / 2;
            window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
            GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }

        if (window == MemoryUtil.NULL) throw new RuntimeException("Failed to create GLFW window");

        GLFW.glfwSetFramebufferSizeCallback(window, (long window, int width, int height) -> {
            Window.width = width;
            Window.height = height;
            if (!renderablesStack.isEmpty()) {
                Renderable renderable = renderablesStack.getLast();
                renderable.resize(new Vector2i(width, height), new Vector2f(1.0f, 1.0f));
            }
        });

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwShowWindow(window);
    }

    public static void renderLoop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            GL46.glViewport(0, 0, width, height);
            GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT | GL46.GL_STENCIL_BUFFER_BIT);
            Renderable renderable = renderablesStack.getLast();
            renderable.render(new Vector2f(0.0f, 0.0f), new Vector2f(1.0f, 1.0f));
            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    }

    public static void toggleFullScreen() {
        maximized = !maximized;
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if (vidMode == null) throw new RuntimeException("Could not get video mode");

        if (maximized) GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, vidMode.width(), vidMode.height(), GLFW.GLFW_DONT_CARE);
        else GLFW.glfwSetWindowMonitor(window, MemoryUtil.NULL, width / 4, height / 4, width / 2, height / 2, GLFW.GLFW_DONT_CARE);
    }

    public static void cleanUp() {
        GLFW.glfwDestroyWindow(window);
    }

    public static Vector2f toPixelCoordinate(Vector2f position) {
        float guiSize = FloatSetting.GUI_SIZE.value();
        return position.mul(guiSize).add((1 - guiSize) * 0.5f, (1 - guiSize) * 0.5f).mul(Window.getWidth(), Window.getHeight());
    }

    public static Vector2f toPixelSize(Vector2f size) {
        float guiSize = FloatSetting.GUI_SIZE.value();
        return size.mul(Window.getWidth(), Window.getHeight()).mul(guiSize);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static long getWindow() {
        return window;
    }

    public static void setTopRenderable(Renderable element) {
        renderablesStack.add(element);
        element.setOnTop();
        GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, GL46.GL_FILL);
    }

    public static void removeTopRenderable() {
        renderablesStack.removeLast();
        if (renderablesStack.isEmpty()) GLFW.glfwSetWindowShouldClose(window, true);
        else renderablesStack.getLast().setOnTop();
    }

    public static void setInput(Input input) {
        input.setInputMode();
        GLFW.glfwSetCursorPosCallback(window, (long window, double xPos, double yPos) -> {
            standardInput.cursorPosCallback(window, xPos, yPos);
            input.cursorPosCallback(window, xPos, yPos);
        });
        GLFW.glfwSetMouseButtonCallback(window, (long window, int button, int action, int mods) -> {
            standardInput.mouseButtonCallback(window, button, action, mods);
            input.mouseButtonCallback(window, button, action, mods);
        });
        GLFW.glfwSetScrollCallback(window, (long window, double xScroll, double yScroll) -> {
            standardInput.scrollCallback(window, xScroll, yScroll);
            input.scrollCallback(window, xScroll, yScroll);
        });
        GLFW.glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods) -> {
            standardInput.keyCallback(window, key, scancode, action, mods);
            input.keyCallback(window, key, scancode, action, mods);
        });
        GLFW.glfwSetCharCallback(window, (long window, int codePoint) -> {
            standardInput.charCallback(window, codePoint);
            input.charCallback(window, codePoint);
        });
    }

    private static int width, height;
    private static long window;
    private static boolean maximized;

    private static final ArrayList<Renderable> renderablesStack = new ArrayList<>();
    private static final StandardWindowInput standardInput = new StandardWindowInput();
}
