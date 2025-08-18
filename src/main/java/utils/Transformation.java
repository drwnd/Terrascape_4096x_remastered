package utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import player.rendering.Camera;

public final class Transformation {

    public static Matrix4f createProjectionRotationMatrix(Camera camera) {
        Vector2f rotation = camera.getRotation();

        Matrix4f matrix = new Matrix4f(camera.getProjectionMatrix());
        matrix.rotate((float) Math.toRadians(rotation.x), X_AXIS).rotate((float) Math.toRadians(rotation.y), Y_AXIS);

        return matrix;
    }

    public static Matrix4f getProjectionViewMatrix(Camera camera) {
        Vector3f position = camera.getPosition().getInChunkPosition();
        return getProjectionViewMatrix(camera, position);
    }

    private static Matrix4f getProjectionViewMatrix(Camera camera, Vector3f translation) {
        Matrix4f matrix = createProjectionRotationMatrix(camera);
        matrix.translate(-translation.x, -translation.y, -translation.z);

        return matrix;
    }

    private Transformation() {
    }

    private static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
    private static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
}
