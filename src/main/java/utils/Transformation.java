package utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import player.Camera;

public final class Transformation {

    public static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
    public static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);

    public static Matrix4f createSkyBoxTransformationMatrix(Camera camera) {
        Vector2f rot = camera.getRotation();

        Matrix4f matrix = new Matrix4f(camera.getProjectionMatrix());
        matrix.rotate((float) Math.toRadians(rot.x), X_AXIS).rotate((float) Math.toRadians(rot.y), Y_AXIS);

        return matrix;
    }

    public static Matrix4f getProjectionViewMatrix(Camera camera) {
        Vector3f pos = camera.getPosition().getInChunkPosition();
        return getTransformationMatrix(camera, pos);
    }

    private static Matrix4f getTransformationMatrix(Camera camera, Vector3f pos) {
        Matrix4f matrix = createSkyBoxTransformationMatrix(camera);
        matrix.translate(-pos.x, -pos.y, -pos.z);

        return matrix;
    }

    private Transformation() {
    }
}
