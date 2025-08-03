package assets.identifiers;

import assets.ObjectGenerator;
import rendering_api.ObjectLoader;

public enum VertexArrayIdentifier {

    TEXT_ROW_VERTEX_ARRAY(ObjectLoader::generateTextRowVertexArray);

    VertexArrayIdentifier(ObjectGenerator generator) {
        this.generator = generator;
    }

    public String getIdentifier() {
        return name();
    }

    public ObjectGenerator getGenerator() {
        return generator;
    }

    private final ObjectGenerator generator;
}
