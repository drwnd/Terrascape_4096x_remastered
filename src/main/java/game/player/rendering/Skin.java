package game.player.rendering;

import core.language.Language;
import core.settings.optionSettings.Option;
import core.utils.FileManager;

import java.io.File;
import java.nio.file.Path;

public final class Skin implements Option {

    public Skin(String skinName) {
        this(Path.of("assets", "skins", skinName).toFile());
    }

    private Skin(File skinFile) {
        this.skinFile = skinFile;
    }

    @Override
    public Option next() {
        File[] languages = FileManager.getSiblings(skinFile);
        int index = (FileManager.indexOf(skinFile, languages) + 1) % languages.length;
        return new Skin(languages[index]);
    }

    @Override
    public Option previous() {
        File[] languages = FileManager.getSiblings(skinFile);
        int index = (FileManager.indexOf(skinFile, languages) - 1 + languages.length) % languages.length;
        return new Skin(languages[index]);
    }

    @Override
    public Option value(String name) {
        return new Language(name);
    }

    @Override
    public int ordinal() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String name() {
        return skinFile.getName();
    }

    @Override
    public String toString() {
        return name();
    }

    private final File skinFile;
}
