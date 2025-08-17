package player;

import org.joml.Vector3f;
import player.movement.Movement;
import rendering_api.Window;
import server.GameHandler;

public final class Player {

    public Player(Position position) {
        meshCollector = new MeshCollector();
        camera = new Camera();
        input = new PlayerInput();
        this.position = position;
        movement = new Movement(new Vector3f());

        Window.setTopRenderable(new Renderer());
        setInput();
    }

    public void updateFrame() {
        meshCollector.loadAllMeshes();

        float fraction = GameHandler.getWorld().getServer().getCurrentGameTickFraction();

        synchronized (this) {
            camera.rotate(input.getCursorMovement());
            Vector3f movementThisTick = movement.getVelocity().mul(fraction);
            Position toRenderPosition = new Position(position);
            toRenderPosition.add(movementThisTick.x, movementThisTick.y, movementThisTick.z);
            camera.setPlayerPositon(toRenderPosition);
        }
    }

    public void updateGameTick() {
        synchronized (this) {
            position = movement.computeNextGameTickPosition(position, camera.getDirection());
        }
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

    private final MeshCollector meshCollector;
    private final Camera camera;
    private final PlayerInput input;
    private final Movement movement;

    private Position position;
}
