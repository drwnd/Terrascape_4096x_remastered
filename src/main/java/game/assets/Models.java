package game.assets;

import com.google.gson.Gson;

import core.assets.AssetManager;
import core.assets.GuiElementData;
import core.assets.identifiers.GuiElementIdentifier;
import core.utils.FileManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public enum Models implements GuiElementIdentifier {
    PLAYER_MODEL;

    @Override
    public GuiElementData getData() {
        Path filepath = AssetManager.getAssetFilepath(Path.of("models", name() + ".json"));
        ModelData modelData = new Gson().fromJson(FileManager.loadJson(filepath), ModelData.class);
        return modelData.toGuiElementData();
    }

    private record ModelData(ModelBox[] boxes) {

        private GuiElementData toGuiElementData() {
            ArrayList<Float> vertices = new ArrayList<>(boxes.length * VERTICES_PER_BOX * 3);
            ArrayList<Integer> transformIndices = new ArrayList<>(boxes.length * VERTICES_PER_BOX * 1);
            ArrayList<Float> textureCoordinates = new ArrayList<>(boxes.length * VERTICES_PER_BOX * 2);

            for (int index = 0; index < boxes.length; index++) {
                ModelBox box = boxes[index];
                generateModelPart(vertices, textureCoordinates, box.position, box.size, false, box.textureCoordinate);
                if (box.hasOuterLayer) generateModelPart(vertices, textureCoordinates, box.position, box.size, true, box.outerTextureCoordinate);
                for (int count = box.hasOuterLayer ? VERTICES_PER_BOX * 2 : VERTICES_PER_BOX * 1; count != 0; count--) transformIndices.add(index);
            }

            return new GuiElementData(
                    new float[][]{toFloatArray(vertices), toFloatArray(textureCoordinates)},
                    new int[][]{toIntArray(transformIndices)},
                    new int[]{3, 2});
        }

        private static void generateModelPart(ArrayList<Float> vert, ArrayList<Float> text, Vector3f position, Vector3i size,
                                              boolean padSize, Vector2f textureCoordinate) {
            float x = size.x * 0.5F, y = size.y * 0.5F, z = size.z * 0.5F;
            float dx = size.x / 64F, dy = size.y / 64F, dz = size.z / 64F;
            float u = textureCoordinate.x, v = textureCoordinate.y;
            position = new Vector3f(position);
            position.x += x;
            position.y += y;
            position.z += z;
            x += padSize ? 0.25F : 0.0F;
            y += padSize ? 0.25F : 0.0F;
            z += padSize ? 0.25F : 0.0F;

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

            vert.addAll(List.of(
                    // front
                    position.x - x, position.y - y, position.z - z,
                    position.x - x, position.y + y, position.z - z,
                    position.x + x, position.y - y, position.z - z,
                    position.x - x, position.y + y, position.z - z,
                    position.x + x, position.y + y, position.z - z,
                    position.x + x, position.y - y, position.z - z,

                    //back
                    position.x - x, position.y + y, position.z + z,
                    position.x - x, position.y - y, position.z + z,
                    position.x + x, position.y - y, position.z + z,
                    position.x + x, position.y + y, position.z + z,
                    position.x - x, position.y + y, position.z + z,
                    position.x + x, position.y - y, position.z + z,

                    // left
                    position.x + x, position.y - y, position.z - z,
                    position.x + x, position.y + y, position.z - z,
                    position.x + x, position.y - y, position.z + z,
                    position.x + x, position.y + y, position.z - z,
                    position.x + x, position.y + y, position.z + z,
                    position.x + x, position.y - y, position.z + z,

                    // right
                    position.x - x, position.y + y, position.z - z,
                    position.x - x, position.y - y, position.z - z,
                    position.x - x, position.y - y, position.z + z,
                    position.x - x, position.y + y, position.z + z,
                    position.x - x, position.y + y, position.z - z,
                    position.x - x, position.y - y, position.z + z,

                    // top
                    position.x - x, position.y + y, position.z - z,
                    position.x - x, position.y + y, position.z + z,
                    position.x + x, position.y + y, position.z + z,
                    position.x + x, position.y + y, position.z - z,
                    position.x - x, position.y + y, position.z - z,
                    position.x + x, position.y + y, position.z + z,

                    // bottom
                    position.x + x, position.y - y, position.z + z,
                    position.x - x, position.y - y, position.z + z,
                    position.x - x, position.y - y, position.z - z,
                    position.x + x, position.y - y, position.z - z,
                    position.x + x, position.y - y, position.z + z,
                    position.x - x, position.y - y, position.z - z
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
