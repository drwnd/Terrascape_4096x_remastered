package game.assets;

import core.assets.Asset;
import core.assets.AssetLoader;
import core.assets.GuiElement;
import core.assets.GuiElementData;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public record Model(GuiElement guiElement, ModelBox[] boxes, Matrix4f[] transforms) implements Asset {

    Model(ModelData data) {
        this(AssetLoader.loadGuiElement(data.toGuiElementData()), data.boxes(), getTransforms(data.boxes()));
    }

    @Override
    public void delete() {

    }

    private static Matrix4f[] getTransforms(ModelBox[] boxes) {
        Matrix4f[] transforms = new Matrix4f[boxes.length];
        for (int index = 0; index < boxes.length; index++)
            transforms[index] = new Matrix4f().translate(boxes[index].position);
        return transforms;
    }

    record ModelData(ModelBox[] boxes) {

        public GuiElementData toGuiElementData() {
            ArrayList<Float> vertices = new ArrayList<>(boxes.length * VERTICES_PER_BOX * 3);
            ArrayList<Float> textureCoordinates = new ArrayList<>(boxes.length * VERTICES_PER_BOX * 2);
            ArrayList<Integer> transformIndices = new ArrayList<>(boxes.length * VERTICES_PER_BOX * 1);

            for (int index = 0; index < boxes.length; index++) {
                ModelBox box = boxes[index];
                generateModelPart(vertices, textureCoordinates, box.size, box.center, false, box.textureCoordinate);
                if (box.hasOuterLayer) generateModelPart(vertices, textureCoordinates, box.size, box.center, true, box.outerTextureCoordinate);
                for (int count = box.hasOuterLayer ? VERTICES_PER_BOX * 2 : VERTICES_PER_BOX * 1; count != 0; count--) transformIndices.add(index);
            }
            return new GuiElementData(
                    new float[][]{toFloatArray(vertices), toFloatArray(textureCoordinates)},
                    new int[][]{toIntArray(transformIndices)},
                    new int[]{3, 2, 1});
        }

        private static void generateModelPart(ArrayList<Float> vert, ArrayList<Float> text, Vector3i size, Vector3f center,
                                              boolean padSize, Vector2f textureCoordinate) {
            float x = size.x * 0.5F, y = size.y * 0.5F, z = size.z * 0.5F;
            float dx = size.x / 64F, dy = size.y / 64F, dz = size.z / 64F;
            float u = textureCoordinate.x, v = textureCoordinate.y;
            x += padSize ? 0.25F : 0.0F;
            y += padSize ? 0.25F : 0.0F;
            z += padSize ? 0.25F : 0.0F;

            vert.addAll(List.of(
                    // front
                    -x, -y, -z,
                    -x, +y, -z,
                    +x, -y, -z,
                    -x, +y, -z,
                    +x, +y, -z,
                    +x, -y, -z,

                    //back
                    -x, +y, +z,
                    -x, -y, +z,
                    +x, -y, +z,
                    +x, +y, +z,
                    -x, +y, +z,
                    +x, -y, +z,

                    // left
                    +x, -y, -z,
                    +x, +y, -z,
                    +x, -y, +z,
                    +x, +y, -z,
                    +x, +y, +z,
                    +x, -y, +z,

                    // right
                    -x, +y, -z,
                    -x, -y, -z,
                    -x, -y, +z,
                    -x, +y, +z,
                    -x, +y, -z,
                    -x, -y, +z,

                    // top
                    -x, +y, -z,
                    -x, +y, +z,
                    +x, +y, +z,
                    +x, +y, -z,
                    -x, +y, -z,
                    +x, +y, +z,

                    // bottom
                    +x, -y, +z,
                    -x, -y, +z,
                    -x, -y, -z,
                    +x, -y, -z,
                    +x, -y, +z,
                    -x, -y, -z
            ));
            text.addAll(List.of(
                    // front
                    u + dx + dz, v + dz + dy,
                    u + dx + dz, v + dz,
                    u + dz, v + dz + dy,
                    u + dx + dz, v + dz,
                    u + dz, v + dz,
                    u + dz, v + dz + dy,

                    //back
                    u + 2 * dz + 1 * dx, v + dz,
                    u + 2 * dz + 1 * dx, v + dy + dz,
                    u + 2 * dz + 2 * dx, v + dy + dz,
                    u + 2 * dz + 2 * dx, v + dz,
                    u + 2 * dz + 1 * dx, v + dz,
                    u + 2 * dz + 2 * dx, v + dy + dz,

                    // left
                    u + dz, v + dy + dz,
                    u + dz, v + dz,
                    u, v + dy + dz,
                    u + dz, v + dz,
                    u, v + dz,
                    u, v + dy + dz,

                    // right
                    u + dx + 1 * dz, v + dz,
                    u + dx + 1 * dz, v + dy + dz,
                    u + dx + 2 * dz, v + dy + dz,
                    u + dx + 2 * dz, v + dz,
                    u + dx + 1 * dz, v + dz,
                    u + dx + 2 * dz, v + dy + dz,

                    // top
                    u + dx + dz, v + dz,
                    u + dx + dz, v,
                    u + dz, v,
                    u + dz, v + dz,
                    u + dx + dz, v + dz,
                    u + dz, v,

                    // bottom
                    u + dz + 2 * dx, v + dz,
                    u + dz + dx, v + dz,
                    u + dz + dx, v,
                    u + dz + 2 * dx, v,
                    u + dz + 2 * dx, v + dz,
                    u + dz + dx, v
            ));
        }

        private static float[] toFloatArray(ArrayList<Float> list) {
            float[] array = new float[list.size()];
            for (int index = 0; index < array.length; index++) array[index] = list.get(index);
            return array;
        }

        private static int[] toIntArray(ArrayList<Integer> list) {
            int[] array = new int[list.size()];
            for (int index = 0; index < array.length; index++) array[index] = list.get(index);
            return array;
        }
    }

    private record ModelBox(Vector3i size, Vector3f center, Vector3f position, Vector2f textureCoordinate, boolean hasOuterLayer,
                            Vector2f outerTextureCoordinate) {
    }

    private static final int VERTICES_PER_BOX = 36;
}
