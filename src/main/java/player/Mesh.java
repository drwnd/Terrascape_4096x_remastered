package player;

import org.joml.Vector3i;

import static utils.Constants.*;

public record Mesh(int[] opaqueVertices, int[] vertexCounts,
                   int[] transparentVertices, int waterVertexCount, int glassVertexCount,
                   int chunkX, int chunkY, int chunkZ, int lod) {

    public Vector3i getWorldCoordinate() {
        return new Vector3i(chunkX << CHUNK_SIZE_BITS, chunkY << CHUNK_SIZE_BITS, chunkZ << CHUNK_SIZE_BITS);
    }
}
