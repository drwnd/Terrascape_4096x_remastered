package assets.identifiers;

import assets.ObjectGenerator;
import rendering_api.ObjectLoader;

public enum BufferIdentifier {

    MODEL_INDEX(ObjectLoader::generateModelIndexBuffer);

    BufferIdentifier(ObjectGenerator generator) {
        this.generator = generator;
    }

    public String getIdentifier() {
        return name() + "_BUFFER";
    }

    public ObjectGenerator getGenerator() {
        return generator;
    }

    private final ObjectGenerator generator;
}
