package utils;

import org.joml.Vector3i;

import static utils.Constants.*;

public final class Utils {

    public static long getChunkId(int chunkX, int chunkY, int chunkZ) {
        return (long) (chunkX & MAX_CHUNKS_XZ) << 40 | (long) (chunkY & MAX_CHUNKS_Y) << 24 | chunkZ & MAX_CHUNKS_XZ;
    }

    public static int getChunkIndex(int chunkX, int chunkY, int chunkZ) {
        chunkX %= RENDERED_WORLD_WIDTH;
        if (chunkX < 0) chunkX += RENDERED_WORLD_WIDTH;

        chunkY %= RENDERED_WORLD_HEIGHT;
        if (chunkY < 0) chunkY += RENDERED_WORLD_HEIGHT;

        chunkZ %= RENDERED_WORLD_WIDTH;
        if (chunkZ < 0) chunkZ += RENDERED_WORLD_WIDTH;

        return (chunkX * RENDERED_WORLD_WIDTH + chunkZ) * RENDERED_WORLD_HEIGHT + chunkY;
    }

    public static boolean outsideChunkKeepDistance(int playerChunkX, int playerChunkY, int playerChunkZ, int chunkX, int chunkY, int chunkZ) {
        return Math.abs(chunkX - playerChunkX) > RENDER_DISTANCE_XZ + RENDER_KEEP_DISTANCE + 1
                || Math.abs(chunkZ - playerChunkZ) > RENDER_DISTANCE_XZ + RENDER_KEEP_DISTANCE + 1
                || Math.abs(chunkY - playerChunkY) > RENDER_DISTANCE_Y + RENDER_KEEP_DISTANCE + 1;
    }

    public static boolean outsideRenderKeepDistance(int playerChunkX, int playerChunkY, int playerChunkZ, int chunkX, int chunkY, int chunkZ) {
        return Math.abs(chunkX - playerChunkX) > RENDER_DISTANCE_XZ + RENDER_KEEP_DISTANCE
                || Math.abs(chunkZ - playerChunkZ) > RENDER_DISTANCE_XZ + RENDER_KEEP_DISTANCE
                || Math.abs(chunkY - playerChunkY) > RENDER_DISTANCE_Y + RENDER_KEEP_DISTANCE;
    }

    public static int mackEven(int value) {
        return value - (value & 1);
    }

    public static int makeOdd(int value) {
        return value + 1 - (value & 1);
    }

    public static int floor(float value) {
        int addend = value < 0 ? -1 : 0;
        return (int) value + addend;
    }

    public static int floor(double value) {
        int addend = value < 0 ? -1 : 0;
        return (int) value + addend;
    }

    public static float fraction(float number) {
        int addend = number < 0 ? 1 : 0;
        return (number - (int) number) + addend;
    }

    public static double smoothInOutQuad(double x, double lowBound, double highBound) {
        // Maps centerX ∈ [lowBound, highBound] to [0, 1]
        x -= lowBound;
        x /= highBound - lowBound;

        if (x < 0.5) return 2 * x * x;
        double oneMinusX = 1 - x;
        return 1 - 2 * oneMinusX * oneMinusX;
    }

    public static int hash(int x, int z, int seed) {
        final int mask = 0x5bd1e995;
        int hash = seed;
        // process first vector element
        int k = x;
        k *= mask;
        k ^= k >> 24;
        k *= mask;
        hash *= mask;
        hash ^= k;
        // process second vector element
        k = z;
        k *= mask;
        k ^= k >> 24;
        k *= mask;
        hash *= mask;
        hash ^= k;
        // some final mixing
        hash ^= hash >> 13;
        hash *= mask;
        hash ^= hash >> 15;
        return hash;
    }

    public static byte[] toByteArray(int i) {
        return new byte[]{(byte) (i >> 24 & 0xFF), (byte) (i >> 16 & 0xFF), (byte) (i >> 8 & 0xFF), (byte) (i & 0xFF)};
    }

    public static byte[] toByteArray(long l) {
        return new byte[]{(byte) (l >> 56 & 0xFF), (byte) (l >> 48 & 0xFF), (byte) (l >> 40 & 0xFF), (byte) (l >> 32 & 0xFF),
                (byte) (l >> 24 & 0xFF), (byte) (l >> 16 & 0xFF), (byte) (l >> 8 & 0xFF), (byte) (l & 0xFF)};
    }

    public static int getInt(byte[] bytes, int startIndex) {
        int result = 0;
        for (int index = startIndex; index < startIndex + 4; index++) {
            int currentByte = bytes[index] & 0xFF;
            result <<= 8;
            result |= currentByte;
        }
        return result;
    }

    public static long getLong(byte[] bytes, int startIndex) {
        long result = 0;
        for (int index = startIndex; index < startIndex + 8; index++) {
            long currentByte = bytes[index] & 0xFF;
            result <<= 8;
            result |= currentByte;
        }
        return result;
    }

    public static Vector3i offsetByNormal(Vector3i value, int side) {
        switch (side) {
            case NORTH -> value.add(0, 0, 1);
            case TOP -> value.add(0, 1, 0);
            case WEST -> value.add(1, 0, 0);
            case SOUTH -> value.add(0, 0, -1);
            case BOTTOM -> value.add(0, -1, 0);
            case EAST -> value.add(-1, 0, 0);
        }
        return value;
    }

    private Utils() {
    }
}
