package utils;

public class Constants {

    // Just pretend it doesn't exist
    public static final float[] SKY_BOX_VERTICES;
    public static final int[] SKY_BOX_INDICES;
    public static final float[] SKY_BOX_TEXTURE_COORDINATES;
    public static final float[] QUAD_TEXTURE_COORDINATES;
    public static final float[] QUAD_VERTICES;

    private Constants() {
    }

    // No like actually, this doesn't exist! Trust me. please...
    static {
        // NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
        // I WARNED YOU!!!
        // WHY DIDN'T YOU LISTEN!??!!?
        // Ok it's actually not THAT bad... (anymore)

        SKY_BOX_VERTICES = new float[]{-1, -1, -1, -1, -1, 1, -1, 1, -1, -1, 1, 1, 1, -1, -1, 1, -1, 1, 1, 1, -1, 1, 1, 1, -1, -1, -1, -1, -1, 1, 1, -1, -1, 1, -1, 1, -1, 1, -1, -1, 1, 1, 1, 1, -1, 1, 1, 1, -1, -1, -1, -1, 1, -1, 1, -1, -1, 1, 1, -1, -1, -1, 1, -1, 1, 1, 1, -1, 1, 1, 1, 1};
        SKY_BOX_INDICES = new int[]{0, 2, 1, 3, 1, 2, 4, 5, 6, 7, 6, 5, 8, 9, 10, 11, 10, 9, 12, 14, 13, 15, 13, 14, 16, 18, 17, 19, 17, 18, 20, 21, 22, 23, 22, 21};
        SKY_BOX_TEXTURE_COORDINATES = new float[]{1, 0.6666667f, 0.75f, 0.6666667f, 1, 0.33333334f, 0.75f, 0.33333334f, 0.25f, 0.6666667f, 0.5f, 0.6666667f, 0.25f, 0.33333334f, 0.5f, 0.33333334f, 0.25f, 1, 0.5f, 1, 0.25f, 0.6666667f, 0.5f, 0.6666667f, 0.25f, 0, 0.5f, 0, 0.25f, 0.33333334f, 0.5f, 0.33333334f, 0, 0.6666667f, 0, 0.33333334f, 0.25f, 0.6666667f, 0.25f, 0.33333334f, 0.75f, 0.6666667f, 0.75f, 0.33333334f, 0.5f, 0.6666667f, 0.5f, 0.33333334f};
        QUAD_TEXTURE_COORDINATES = new float[]{0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1};
        QUAD_VERTICES = new float[]{0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0};
    }
}
