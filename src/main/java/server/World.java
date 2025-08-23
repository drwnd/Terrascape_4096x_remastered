package server;

import utils.Utils;

import static utils.Constants.RENDERED_WORLD_HEIGHT;
import static utils.Constants.RENDERED_WORLD_WIDTH;

public final class World {

    public World() {
        chunks = new Chunk[RENDERED_WORLD_WIDTH * RENDERED_WORLD_HEIGHT * RENDERED_WORLD_WIDTH];
        server = new Server();
    }

    public void init() {
        startTicks();
    }

    public Chunk getChunk(int chunkX, int chunkY, int chunkZ, int lod) {
        return chunks[Utils.getChunkIndex(chunkX, chunkY, chunkZ)];
    }

    public Chunk getChunk(int chunkIndex, int lod) {
        return chunks[chunkIndex];
    }

    public Chunk[] getLod(int lod) {
        return chunks;
    }

    public void storeChunk(Chunk chunk) {
        Chunk previousChunk = chunks[chunk.getIndex()];
        if (previousChunk != null) previousChunk.cleanUp();

        chunks[chunk.getIndex()] = chunk;
    }

    public void setNull(int chunkIndex, int lod) {
        Chunk previousChunk = chunks[chunkIndex];
        if (previousChunk != null) previousChunk.cleanUp();

        chunks[chunkIndex] = null;
    }

    public void pauseTicks() {
        server.pauseTicks();
    }

    public void startTicks() {
        server.startTicks();
    }

    public void cleanUp() {
        pauseTicks();
        server.cleanUp();
    }

    public Server getServer() {
        return server;
    }

    private final Chunk[] chunks;
    private final Server server;
}
