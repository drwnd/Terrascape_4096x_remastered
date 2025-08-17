package server;

import player.MeshGenerator;
import utils.Utils;

import static utils.Constants.*;

public final class World {

    public World() {
        chunks = new Chunk[RENDERED_WORLD_WIDTH * RENDERED_WORLD_HEIGHT * RENDERED_WORLD_WIDTH];
    }

    public void init() {
        for (int chunkX = 0; chunkX < RENDERED_WORLD_WIDTH; chunkX++)
            for (int chunkZ = 0; chunkZ < RENDERED_WORLD_WIDTH; chunkZ++)
                for (int chunkY = 0; chunkY < RENDERED_WORLD_HEIGHT; chunkY++) {
                    Chunk chunk = new Chunk(chunkX, chunkY, chunkZ);
                    WorldGeneration.generate(chunk);
                    chunks[chunk.getIndex()] = chunk;
                }

        MeshGenerator generator = new MeshGenerator();
        for (Chunk chunk : chunks) {
            generator.setChunk(chunk);
            generator.generateMesh();
        }
    }

    public Chunk getChunk(int chunkX, int chunkY, int chunkZ, int lod) {
        return chunks[Utils.getChunkIndex(chunkX, chunkY, chunkZ)];
    }

    public void storeChunk(Chunk chunk) {
        Chunk previousChunk = chunks[chunk.getIndex()];
        chunks[chunk.getIndex()] = chunk;

        if (previousChunk != null) previousChunk.cleanUp();
    }

    public void pauseTicks() {

    }

    public void startTicks() {

    }

    public void cleanUp() {
        pauseTicks();
    }

    private final Chunk[] chunks;
}
