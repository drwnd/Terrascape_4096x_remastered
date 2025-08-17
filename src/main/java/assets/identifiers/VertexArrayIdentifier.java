package assets.identifiers;

import assets.ObjectGenerator;
import rendering_api.ObjectLoader;

public enum VertexArrayIdentifier {

    TEXT_ROW(ObjectLoader::generateTextRowVertexArray),
    SKYBOX(ObjectLoader::generateSkyboxVertexArray);

    VertexArrayIdentifier(ObjectGenerator generator) {
        this.generator = generator;
    }

    public String getIdentifier() {
        return name() + "_VERTEX_ARRAY";
    }

    public ObjectGenerator getGenerator() {
        return generator;
    }

    private final ObjectGenerator generator;
}
