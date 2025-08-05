package assets;

import assets.identifiers.BufferIdentifier;
import org.lwjgl.opengl.GL46;

public class Buffer extends Asset {

    public Buffer(ObjectGenerator generator) {
        this.id = generator.generateObject();
    }

    @Override
    public void reload(String identifier) {
        System.out.printf("Reloading Buffer %s%n", identifier);
        GL46.glDeleteBuffers(id);
        id = BufferIdentifier.valueOf(identifier).getGenerator().generateObject();
    }

    public int getID() {
        return id;
    }

    private int id;
}

