package player.interaction;

import org.lwjgl.glfw.GLFW;
import server.Game;
import settings.FloatSetting;
import settings.KeySetting;

import static utils.Constants.AIR;
import static utils.Constants.CHUNK_SIZE;

public final class InteractionHandler {

    public void handleInput(int button, int action) {
        if (action == GLFW.GLFW_PRESS && button == KeySetting.INCREASE_BREAK_PLACE_SIZE.value()) placeBreakSize = Math.min(CHUNK_SIZE, placeBreakSize << 1);
        if (action == GLFW.GLFW_PRESS && button == KeySetting.DECREASE_BREAK_PLACE_SIZE.value()) placeBreakSize = Math.max(1, placeBreakSize >> 1);

        if (button == KeySetting.DESTROY.value()) destroyHeld = action == GLFW.GLFW_PRESS;
        if (button == KeySetting.USE.value()) useHeld = action == GLFW.GLFW_PRESS;
    }

    public void updateGameTick() {
        handleDestroy();
        handleUse();
    }

    private void handleUse() {
        long currentGameTick = Game.getServer().getCurrentGameTick();
        if (!useHeld || currentGameTick - lastUseAction < FloatSetting.BREAK_PLACE_INTERVALL.value()) return;

        Target target = Target.getPlayerTarget();
        if (target == null) return;

        byte material = Game.getPlayer().getHeldMaterial();
        if (Game.getServer().requestBreakPlaceInteraction(target.positionWithOffset(), placeBreakSize, material)) lastUseAction = currentGameTick;
    }

    private void handleDestroy() {
        long currentGameTick = Game.getServer().getCurrentGameTick();
        if (!destroyHeld || currentGameTick - lastDestroyAction < FloatSetting.BREAK_PLACE_INTERVALL.value()) return;

        Target target = Target.getPlayerTarget();
        if (target == null) return;

        if (Game.getServer().requestBreakPlaceInteraction(target.position(), placeBreakSize, AIR)) lastDestroyAction = currentGameTick;
    }

    private boolean destroyHeld = false, useHeld = false;
    private long lastDestroyAction, lastUseAction;
    private int placeBreakSize = 16;
}
