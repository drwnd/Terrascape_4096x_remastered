package server;

import java.io.File;

public final class WorldInitializer {

    public static void play(File saveFile) {
        FileManager.loadUniversalFiles(saveFile);

        FileManager.loadPlayer();
        World.startTicks();
    }
}
