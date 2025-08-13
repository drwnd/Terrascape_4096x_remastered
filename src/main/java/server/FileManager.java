package server;

import player.Player;

import java.io.File;

public final class FileManager {

    public static void loadPlayer() {
        Player.init();
    }

    public static void loadUniversalFiles(File saveFile) {
        FileManager.saveFile = saveFile;
        if (!saveFile.exists()) //noinspection ResultOfMethodCallIgnored
            saveFile.mkdirs();

        chunksFile = new File(saveFile.getPath() + "/chunks");
        if (!chunksFile.exists()) //noinspection ResultOfMethodCallIgnored
            chunksFile.mkdirs();

        settingsFile = new File(saveFile.getPath() + "/settings");
    }

    private static File saveFile, chunksFile, settingsFile;

    private FileManager() {
    }
}
