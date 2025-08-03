package assets;

import org.lwjgl.opengl.GL46;

public class VertexArray extends Asset {

    public VertexArray(ObjectGenerator generator) {
        this.id = generator.generateObject();
        this.generator = generator;
    }

    @Override
    public void reload(String identifier) {
        System.out.printf("Reloading VertexArray %s%n", identifier);
        GL46.glDeleteVertexArrays(id);
        id = generator.generateObject();
    }

    public int getID() {
        return id;
    }

    private int id;
    private final ObjectGenerator generator;
}

