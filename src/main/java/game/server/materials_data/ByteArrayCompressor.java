package game.server.materials_data;

import core.utils.ByteArrayList;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static game.server.materials_data.MaterialsData.*;

final class ByteArrayCompressor {

    private static Unsafe unsafe = null;
    private static int longArrayClassPointer;
    private static int byteArrayClassPointer;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);

            longArrayClassPointer = unsafe.getInt(new long[0], 8);
            byteArrayClassPointer = unsafe.getInt(new byte[0], 8);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private ByteArrayCompressor() {

    }

    static void compressMaterials(ByteArrayList data, byte[] uncompressedMaterials, int sizeBits) {
        if (uncompressedMaterials == null) return;
        int length = 1 << sizeBits * 3;
        unsafe.putInt(uncompressedMaterials, 12, length / 8);
        unsafe.putInt(uncompressedMaterials, 8, longArrayClassPointer);

        try {
            long[] longMaterials = (long[]) getObject(uncompressedMaterials);
            compressMaterials(data, longMaterials, sizeBits, 0, 0, 0, 0);
        } catch (Exception exception) {
            exception.printStackTrace();
            data.add((byte) (HOMOGENOUS | CONTAINS_SELF_OCCLUDING));
            data.add((byte) 0);
        }

        unsafe.putInt(uncompressedMaterials, 12, length);
        unsafe.putInt(uncompressedMaterials, 8, byteArrayClassPointer);
    }

    public static Object getObject(Object object) {
        Object[] array = new Object[]{object};
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        return unsafe.getObject(array, baseOffset);
    }

    private static int compressMaterials(ByteArrayList data, long[] uncompressedMaterials, int sizeBits, int startIndex, int inChunkX, int inChunkY, int inChunkZ) {
        if (isHomogenous(MaterialsData.getUncompressedIndex(inChunkX, inChunkY, inChunkZ), 1 << sizeBits * 3, uncompressedMaterials))
            return addHomogenous(data, uncompressedMaterials, inChunkX, inChunkY, inChunkZ);
        if (sizeBits <= 1) {
            long materials = uncompressedMaterials[getUncompressedIndex(inChunkX, inChunkY, inChunkZ) >> 3];
            data.add((byte) (getTypes(materials) | DETAIL));
            data.add((byte) materials);
            data.add((byte) (materials >> 16));
            data.add((byte) (materials >> 8));
            data.add((byte) (materials >> 24));
            data.add((byte) (materials >> 32));
            data.add((byte) (materials >> 48));
            data.add((byte) (materials >> 40));
            data.add((byte) (materials >> 56));
            return DETAIL_BYTE_SIZE;
        }

        int nextSize = 1 << --sizeBits;
        int offset = SPLITTER_BYTE_SIZE, index = data.size();
        data.add(SPLITTER);
        data.pad(SPLITTER_BYTE_SIZE - 1);

        offset += compressMaterials(data, uncompressedMaterials, sizeBits, startIndex + offset, inChunkX, inChunkY, inChunkZ);
        setOffset(data, offset, startIndex + 1);
        offset += compressMaterials(data, uncompressedMaterials, sizeBits, startIndex + offset, inChunkX, inChunkY, inChunkZ + nextSize);
        setOffset(data, offset, startIndex + 4);
        offset += compressMaterials(data, uncompressedMaterials, sizeBits, startIndex + offset, inChunkX, inChunkY + nextSize, inChunkZ);
        setOffset(data, offset, startIndex + 7);
        offset += compressMaterials(data, uncompressedMaterials, sizeBits, startIndex + offset, inChunkX, inChunkY + nextSize, inChunkZ + nextSize);
        setOffset(data, offset, startIndex + 10);
        offset += compressMaterials(data, uncompressedMaterials, sizeBits, startIndex + offset, inChunkX + nextSize, inChunkY, inChunkZ);
        setOffset(data, offset, startIndex + 13);
        offset += compressMaterials(data, uncompressedMaterials, sizeBits, startIndex + offset, inChunkX + nextSize, inChunkY, inChunkZ + nextSize);
        setOffset(data, offset, startIndex + 16);
        offset += compressMaterials(data, uncompressedMaterials, sizeBits, startIndex + offset, inChunkX + nextSize, inChunkY + nextSize, inChunkZ);
        setOffset(data, offset, startIndex + 19);
        offset += compressMaterials(data, uncompressedMaterials, sizeBits, startIndex + offset, inChunkX + nextSize, inChunkY + nextSize, inChunkZ + nextSize);

        data.set((byte) (getSplitterTypes(data, index) | SPLITTER), index);
        return offset;
    }

    private static boolean isHomogenous(int startIndex, int length, long[] uncompressedMaterials) {
        long target = uncompressedMaterials[startIndex >> 3];
        if (!isHomogenous(target)) return false;
        int endIndex = startIndex + length >> 3;
        for (int index = startIndex + 8 >> 3; index < endIndex; index++)
            if (uncompressedMaterials[index] != target) return false;
        return true;
    }

    private static int addHomogenous(ByteArrayList data, long[] uncompressedMaterials, int inChunkX, int inChunkY, int inChunkZ) {
        int index = getUncompressedIndex(inChunkX, inChunkY, inChunkZ);
        byte material = (byte) (uncompressedMaterials[index >> 3] >> (index & 7) * 8);
        data.add((byte) (getType(material) | HOMOGENOUS));
        data.add(material);
        return HOMOGENOUS_BYTE_SIZE;
    }

    private static boolean isHomogenous(long materials) {
        byte material = (byte) materials;
        return (byte) (materials >> 8) == material
                && (byte) (materials >> 16) == material
                && (byte) (materials >> 24) == material
                && (byte) (materials >> 32) == material
                && (byte) (materials >> 40) == material
                && (byte) (materials >> 48) == material
                && (byte) (materials >> 56) == material;
    }

    private static int getTypes(long materials) {
        return getType((byte) materials)
                | getType((byte) (materials >> 8))
                | getType((byte) (materials >> 16))
                | getType((byte) (materials >> 24))
                | getType((byte) (materials >> 32))
                | getType((byte) (materials >> 40))
                | getType((byte) (materials >> 48))
                | getType((byte) (materials >> 56));
    }
}
