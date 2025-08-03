package assets;

import org.lwjgl.opengl.GL46;

public class Buffer extends Asset {

    public Buffer(ObjectGenerator generator) {
        this.id = generator.generateObject();
        this.generator = generator;
    }

    @Override
    public void reload(String identifier) {
        System.out.printf("Reloading Buffer %s%n", identifier);
        GL46.glDeleteBuffers(id);
        id = generator.generateObject();
    }

    public int getID() {
        return id;
    }

    private int id;
    private final ObjectGenerator generator;
}

