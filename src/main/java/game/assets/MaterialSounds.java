package game.assets;

import core.assets.AssetLoader;
import core.assets.AssetManager;
import core.assets.SoundCollection;
import core.assets.identifiers.SoundCollectionIdentifier;
import core.assets.identifiers.SoundIdentifier;
import core.sound.Sound;

import java.io.File;
import java.util.ArrayList;

public class MaterialSounds implements SoundCollectionIdentifier {

    @Override
    public SoundCollection generateAsset() {
        if (folderPath == null) return new SoundCollection(new SingleSoundIdentifier[0]);
        File soundFolder = new File(AssetManager.getAssetFilepath("sounds/" + folderPath));
        if (!soundFolder.exists() || !soundFolder.isDirectory()) return new SoundCollection(new SingleSoundIdentifier[0]);

        File[] files = soundFolder.listFiles();
        if (files == null) return new SoundCollection(new SingleSoundIdentifier[0]);

        ArrayList<SoundIdentifier> identifiers = new ArrayList<>();
        for (File file : files) {
            if (!file.getName().startsWith(fileNamePrefix)) continue;
            identifiers.add(new SingleSoundIdentifier(file.getPath(), gainMultiplier, pitchMultiplier));
        }

        return new SoundCollection(identifiers.toArray(new SoundIdentifier[0]));
    }

    @SuppressWarnings({"unused", "FieldMayBeFinal"})
    private String folderPath = null, fileNamePrefix = null;
    @SuppressWarnings({"unused", "FieldMayBeFinal", "FieldCanBeLocal"})
    private float gainMultiplier = 1.0F, pitchMultiplier = 1.0F;

    private record SingleSoundIdentifier(String filePath, float gainMultiplier, float pitchMultiplier) implements SoundIdentifier {
        @Override
        public Sound generateAsset() {
            return new Sound(AssetLoader.loadSound(filePath, false), gainMultiplier, pitchMultiplier);
        }
    }
}
