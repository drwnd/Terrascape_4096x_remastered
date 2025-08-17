package player;

import rendering_api.Window;

public final class Player {

    public Player(Position position) {
        renderer = new Renderer();
        meshCollector = new MeshCollector();
        camera = new Camera();
        input = new PlayerInput();
        Window.setTopRenderable(renderer);
        setInput();
        actualPosition = position;
        toRenderPosition = new Position(position);
    }

    public void updateFrame() {
        meshCollector.loadAllMeshes();
        input.rotateCamera();
    }

    public void setInput() {
        Window.setInput(input);
    }

    public void cleanUp() {

    }

    public MeshCollector getMeshCollector() {
        return meshCollector;
    }

    public Camera getCamera() {
        return camera;
    }

    public Position getToRenderPosition() {
        return toRenderPosition;
    }

    private final Renderer renderer;
    private final MeshCollector meshCollector;
    private final Camera camera;
    private final PlayerInput input;

    private final Position toRenderPosition;
    private final Position actualPosition;
}
