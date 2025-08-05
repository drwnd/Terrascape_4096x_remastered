package assets;

import assets.identifiers.VertexArrayIdentifier;
import org.lwjgl.opengl.GL46;

public class VertexArray extends Asset {

    public VertexArray(ObjectGenerator generator) {
        this.id = generator.generateObject();
    }

    @Override
    public void reload(String identifier) {
        System.out.printf("Reloading VertexArray %s%n", identifier);
        GL46.glDeleteVertexArrays(id);
        id = VertexArrayIdentifier.valueOf(identifier).getGenerator().generateObject();
    }

    public int getID() {
        return id;
    }

    private int id;
}

