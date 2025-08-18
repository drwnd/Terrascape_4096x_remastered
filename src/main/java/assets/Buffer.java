package assets;

import org.lwjgl.opengl.GL46;

public class Buffer extends Asset {

    public Buffer(ObjectGenerator generator) {
        this.id = generator.generateObject();
    }

    @Override
    public void delete() {
        GL46.glDeleteBuffers(id);
    }

    public int getID() {
        return id;
    }

    private final int id;
}

