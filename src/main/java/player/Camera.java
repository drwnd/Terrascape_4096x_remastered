package player;

import org.joml.*;
import rendering_api.Window;
import settings.FloatSetting;

import java.lang.Math;

import static utils.Constants.Z_FAR;
import static utils.Constants.Z_NEAR;

public final class Camera {

    public Camera() {
        position = new Position(new Vector3i(), new Vector3f());
        rotation = new Vector2f(0.0f, 0.0f);
    }

    public void updateProjectionMatrix() {
        float aspectRatio = (float) Window.getWidth() / Window.getHeight();
        projectionMatrix.setPerspective(FloatSetting.FOV.defaultValue(), aspectRatio, Z_NEAR, Z_FAR);
    }

    public Vector3f getDirection() {

        float rotationXRadians = (float) Math.toRadians(rotation.y);
        float rotationYRadians = (float) Math.toRadians(rotation.x);

        float x = (float) Math.sin(rotationXRadians);
        float y = (float) -Math.sin(rotationYRadians);
        float z = (float) -Math.cos(rotationXRadians);

        float normalizer = (float) Math.sqrt(1 - y * y);

        x *= normalizer;
        z *= normalizer;

        return new Vector3f(x, y, z);
    }

    private void moveRotation(float yaw, float pitch) {
        rotation.x += pitch;
        rotation.y += yaw;

        rotation.x = Math.max(-90, Math.min(rotation.x, 90));
        rotation.y %= 360.0f;
    }

    public void rotate(Vector2i cursorMovement) {
        float sensitivityFactor = FloatSetting.SENSITIVITY.value() * 0.6f + 0.2f;
        sensitivityFactor = 1.2f * sensitivityFactor * sensitivityFactor * sensitivityFactor;
        float rotationX = cursorMovement.x * sensitivityFactor;
        float rotationY = cursorMovement.y * sensitivityFactor;

        moveRotation(rotationX, -rotationY);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Position getPosition() {
        return position;
    }

    public void setPlayerPositon(Position playerPositon) {
        position = playerPositon;
    }

    public Vector2f getRotation() {
        return rotation;
    }

    private Position position;
    private final Vector2f rotation;

    private final Matrix4f projectionMatrix = new Matrix4f();
}
