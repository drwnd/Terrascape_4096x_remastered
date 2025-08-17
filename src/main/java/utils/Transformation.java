package utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import player.Camera;

import static utils.Constants.*;

public final class Transformation {

    public static Matrix4f createSkyBoxTransformationMatrix(Camera camera) {
        Vector2f rot = camera.getRotation();

        Matrix4f matrix = new Matrix4f(camera.getProjectionMatrix());
        matrix.rotate((float) Math.toRadians(rot.x), X_AXIS).rotate((float) Math.toRadians(rot.y), Y_AXIS);

        return matrix;
    }

    public static Matrix4f getProjectionViewMatrix(Camera camera) {
        Vector3f pos = camera.getPosition();
        Vector2f rot = camera.getRotation();

        Matrix4f matrix = new Matrix4f(camera.getProjectionMatrix());
        matrix.rotate((float) Math.toRadians(rot.x), X_AXIS).rotate((float) Math.toRadians(rot.y), Y_AXIS);
        matrix.translate(
                -Utils.fraction(pos.x / CHUNK_SIZE) * CHUNK_SIZE,
                -Utils.fraction(pos.y / CHUNK_SIZE) * CHUNK_SIZE,
                -Utils.fraction(pos.z / CHUNK_SIZE) * CHUNK_SIZE);

        return matrix;
    }

    public static Matrix4f getFrustumCullingMatrix(Camera camera) {
        Vector3f pos = camera.getPosition();
        Vector2f rot = camera.getRotation();

        Matrix4f matrix = new Matrix4f(camera.getProjectionMatrix());
        matrix.rotate((float) Math.toRadians(rot.x), X_AXIS).rotate((float) Math.toRadians(rot.y), Y_AXIS);
        matrix.translate(-pos.x, -pos.y, -pos.z);

        return matrix;
    }

    private Transformation() {
    }

    private static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
    private static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
}
