package server;

import static utils.Constants.*;

public final class WorldGeneration {

    public static long SEED;

    public static void generate(Chunk chunk) {
        byte[] materials = new byte[CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE];
        for (int counter = 0; counter < 1000; counter ++)
            materials[(int) (Math.random() * materials.length)] = STONE;
        chunk.getMaterials().setToUncompressedMaterials(materials);
    }
}
