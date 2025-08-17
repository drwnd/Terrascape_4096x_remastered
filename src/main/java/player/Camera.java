package player;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import rendering_api.Window;
import settings.FloatSetting;

import static utils.Constants.*;

public final class Camera {

    public Camera() {
        position = new Vector3f(0.0f, 0.0f, 0.0f);
        rotation = new Vector2f(0.0f, 0.0f);
    }

    public void updateProjectionMatrix() {
        float aspectRatio = (float) Window.getWidth() / Window.getHeight();
        projectionMatrix.setPerspective(FloatSetting.FOV.defaultValue(), aspectRatio, Z_NEAR, Z_FAR);
    }

    private void updateProjectionMatrix(float fov) {
        float aspectRatio = (float) Window.getWidth() / Window.getHeight();
        projectionMatrix.setPerspective(fov, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Vector3f getDirection() {

        float rotationXRadians = (float) Math.toRadians(rotation.y);
        float rotationYRadians = (float) Math.toRadians(rotation.x);

        float x = (float) Math.sin(rotationXRadians);
        float y = (float) -Math.sin(rotationYRadians);
        float z = (float) -Math.cos(rotationXRadians);

        float v = (float) Math.sqrt(1 - y * y);

        x *= v;
        z *= v;

        return new Vector3f(x, y, z);
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void moveRotation(float yaw, float pitch) {
        rotation.x += pitch;
        rotation.y += yaw;

        rotation.x = Math.max(-90, Math.min(rotation.x, 90));
        rotation.y %= 360.0f;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector2f getRotation() {
        return rotation;
    }

    private final Vector3f position;
    private final Vector2f rotation;

    private final Matrix4f projectionMatrix = new Matrix4f();
}
