package player;

import org.joml.Vector3i;

public record TransparentModel(int X, int Y, int Z, int LOD, int verticesBuffer, int waterVertexCount, int glassVertexCount) {

    public TransparentModel(Vector3i position, int waterVertexCount, int glassVertexCount, int verticesBuffer, int lod) {
        this(position.x, position.y, position.z, lod, verticesBuffer, waterVertexCount, glassVertexCount);
    }
}

