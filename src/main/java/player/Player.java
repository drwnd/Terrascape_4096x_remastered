package player;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import player.movement.Movement;
import player.rendering.Camera;
import player.rendering.MeshCollector;
import player.rendering.Renderer;
import rendering_api.Window;
import server.Game;
import settings.KeySetting;
import utils.Position;

public final class Player {

    public Player(Position position) {
        meshCollector = new MeshCollector();
        camera = new Camera();
        input = new PlayerInput();
        this.position = position;
        movement = new Movement(new Vector3f());
        renderer = new Renderer();

        Window.setTopRenderable(renderer);
        setInput();
    }

    public void updateFrame() {
        meshCollector.uploadAllMeshes();
        meshCollector.deleteOldMeshes();

        float fraction = Game.getWorld().getServer().getCurrentGameTickFraction();
        fraction = Math.clamp(fraction, 0.0f, 1.0f);

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

    public void registerKey(int key, int action) {
        movement.registerKey(key, action);
        if (key == KeySetting.DEBUG_MENU.value() && action == GLFW.GLFW_PRESS) renderer.toggleDebugScreen();
    }

    public Position getPosition() {
        synchronized (this) {
            return new Position(position);
        }
    }

    private final MeshCollector meshCollector;
    private final Camera camera;
    private final PlayerInput input;
    private final Movement movement;
    private final Renderer renderer;

    private Position position;
}
