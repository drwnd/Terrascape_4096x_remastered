package assets.identifiers;

import assets.ObjectGenerator;
import rendering_api.ObjectLoader;

public enum BufferIdentifier {

    MODEL_INDEX_BUFFER(ObjectLoader::generateModelIndexBuffer);

    BufferIdentifier(ObjectGenerator generator) {
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
