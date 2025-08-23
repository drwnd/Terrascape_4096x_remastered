package server;

import player.Player;
import rendering_api.Window;

import java.io.File;

public final class Game {

    public static void play(File saveFile) {
        FileManager.loadUniversalFiles(saveFile);
        Material.init();

        player = FileManager.loadPlayer();
        world = new World();
        world.init();
    }

    public static void quit() {
        Window.removeTopRenderable();
        world.cleanUp();
        player.cleanUp();
        Window.removeTopRenderable();

        player = null;
        world = null;
    }

    public static Player getPlayer() {
        return player;
    }

    public static World getWorld() {
        return world;
    }

    private static Player player;
    private static World world;
}
