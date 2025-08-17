package server;

import utils.Utils;

import static utils.Constants.*;

public final class Chunk {

    public final int X, Y, Z, LOD;
    public final long ID;
    private final int INDEX;

    public Chunk(int chunkX, int chunkY, int chunkZ) {
        materials = new MaterialsData(AIR);
        X = chunkX;
        Y = chunkY;
        Z = chunkZ;
        INDEX = Utils.getChunkIndex(X, Y, Z);
        ID = Utils.getChunkId(X, Y, Z);
        LOD = 0;
    }

    public byte getSaveMaterial(int inChunkX, int inChunkY, int inChunkZ) {
        return materials.getMaterial(inChunkX, inChunkY, inChunkZ);
    }

    public byte getMaterial(int inChunkX, int inChunkY, int inChunkZ) {
        if (inChunkX < 0) {
            Chunk neighbor = GameHandler.getWorld().getChunk(X - 1, Y, Z, LOD);
            if (neighbor == null) return OUT_OF_WORLD;
            return neighbor.getSaveMaterial(CHUNK_SIZE + inChunkX, inChunkY, inChunkZ);
        } else if (inChunkX >= CHUNK_SIZE) {
            Chunk neighbor = GameHandler.getWorld().getChunk(X + 1, Y, Z, LOD);
            if (neighbor == null) return OUT_OF_WORLD;
            return neighbor.getSaveMaterial(inChunkX - CHUNK_SIZE, inChunkY, inChunkZ);
        }
        if (inChunkY < 0) {
            Chunk neighbor = GameHandler.getWorld().getChunk(X, Y - 1, Z, LOD);
            if (neighbor == null) return OUT_OF_WORLD;
            return neighbor.getSaveMaterial(inChunkX, CHUNK_SIZE + inChunkY, inChunkZ);
        } else if (inChunkY >= CHUNK_SIZE) {
            Chunk neighbor = GameHandler.getWorld().getChunk(X, Y + 1, Z, LOD);
            if (neighbor == null) return OUT_OF_WORLD;
            return neighbor.getSaveMaterial(inChunkX, inChunkY - CHUNK_SIZE, inChunkZ);
        }
        if (inChunkZ < 0) {
            Chunk neighbor = GameHandler.getWorld().getChunk(X, Y, Z - 1, LOD);
            if (neighbor == null) return OUT_OF_WORLD;
            return neighbor.getSaveMaterial(inChunkX, inChunkY, CHUNK_SIZE + inChunkZ);
        } else if (inChunkZ >= CHUNK_SIZE) {
            Chunk neighbor = GameHandler.getWorld().getChunk(X, Y, Z + 1, LOD);
            if (neighbor == null) return OUT_OF_WORLD;
            return neighbor.getSaveMaterial(inChunkX, inChunkY, inChunkZ - CHUNK_SIZE);
        }

        return getSaveMaterial(inChunkX, inChunkY, inChunkZ);
    }

    public int getIndex() {
        return INDEX;
    }

    public MaterialsData getMaterials() {
        return materials;
    }

    public void cleanUp() {

    }

    private final MaterialsData materials;
}
