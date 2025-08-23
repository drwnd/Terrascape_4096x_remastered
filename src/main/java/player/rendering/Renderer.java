package player.rendering;

import assets.AssetManager;
import assets.identifiers.ShaderIdentifier;
import assets.identifiers.TextureIdentifier;
import assets.identifiers.VertexArrayIdentifier;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import player.Player;
import renderables.Renderable;
import renderables.TextElement;
import rendering_api.shaders.Shader;
import rendering_api.shaders.TextShader;
import server.Chunk;
import server.Game;
import server.WorldGeneration;
import settings.FloatSetting;
import settings.ToggleSetting;
import utils.Position;
import utils.Transformation;
import utils.Utils;

import java.awt.*;
import java.util.ArrayList;

import static utils.Constants.*;

public final class Renderer extends Renderable {
    public Renderer() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f));
        allowScaling(false);
    }

    public void toggleDebugScreen() {
        debugScreenOpen = !debugScreenOpen;
    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {
        Player player = Game.getPlayer();
        Camera camera = player.getCamera();
        player.updateFrame();

        Matrix4f projectionViewMatrix = Transformation.getProjectionViewMatrix(camera);
        Position cameraPosition = player.getCamera().getPosition();

        setupRenderState();
        renderSkybox(camera);
        renderOpaqueGeometry(cameraPosition, projectionViewMatrix, player);
        renderWater(cameraPosition, projectionViewMatrix, player);
        if (debugScreenOpen) renderDebugInfo();
    }

    private static void setupRenderState() {
        Game.getPlayer().getCamera().updateProjectionMatrix();
        GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, ToggleSetting.X_RAY.value() ? GL46.GL_LINE : GL46.GL_FILL);
        GLFW.glfwSwapInterval(ToggleSetting.V_SYNC.value() ? 1 : 0);
    }

    private static void renderSkybox(Camera camera) {
        GL46.glDisable(GL46.GL_BLEND);
        Shader shader = AssetManager.getShader(ShaderIdentifier.SKYBOX);

        shader.bind();
        shader.setUniform("textureAtlas1", 0);
        shader.setUniform("textureAtlas2", 1);
        shader.setUniform("time", 1.0f);
        shader.setUniform("projectionViewMatrix", Transformation.createProjectionRotationMatrix(camera));

        GL46.glBindVertexArray(AssetManager.getVertexArray(VertexArrayIdentifier.SKYBOX).getID());
        GL46.glEnableVertexAttribArray(0);
        GL46.glEnableVertexAttribArray(1);

        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, AssetManager.getTexture(TextureIdentifier.NIGHT_SKY).getID());
        GL46.glActiveTexture(GL46.GL_TEXTURE1);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, AssetManager.getTexture(TextureIdentifier.DAY_SKY).getID());

        GL46.glDepthMask(false);
        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glDisable(GL46.GL_CULL_FACE);

        GL46.glDrawElements(GL46.GL_TRIANGLES, SKY_BOX_INDICES.length, GL46.GL_UNSIGNED_INT, 0);

        GL46.glDepthMask(true);
    }

    private static void renderOpaqueGeometry(Position playerPosition, Matrix4f projectionViewMatrix, Player player) {
        int playerChunkX = Utils.floor(playerPosition.intPosition().x) >> CHUNK_SIZE_BITS;
        int playerChunkY = Utils.floor(playerPosition.intPosition().y) >> CHUNK_SIZE_BITS;
        int playerChunkZ = Utils.floor(playerPosition.intPosition().z) >> CHUNK_SIZE_BITS;

        Shader shader = AssetManager.getShader(ShaderIdentifier.OPAQUE);
        shader.bind();
        shader.setUniform("projectionViewMatrix", projectionViewMatrix);
        shader.setUniform("iCameraPosition",
                playerPosition.intPosition().x & ~CHUNK_SIZE_MASK,
                playerPosition.intPosition().y & ~CHUNK_SIZE_MASK,
                playerPosition.intPosition().z & ~CHUNK_SIZE_MASK);
        shader.setUniform("textureAtlas", 0);
        shader.setUniform("propertiesTexture", 1);

        GL46.glBindVertexArray(AssetManager.getVertexArray(VertexArrayIdentifier.SKYBOX).getID()); // Just bind something IDK
        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glDisable(GL46.GL_BLEND);
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, AssetManager.getTexture(TextureIdentifier.MATERIALS).getID());
        GL46.glActiveTexture(GL46.GL_TEXTURE1);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, AssetManager.getTexture(TextureIdentifier.PROPERTIES).getID());

        for (OpaqueModel model : player.getMeshCollector().getOpaqueModels(0)) {
            if (model == null || !model.containsGeometry()) continue;
            int[] toRenderVertexCounts = model.getVertexCounts(playerChunkX, playerChunkY, playerChunkZ);

            GL46.glBindBufferBase(GL46.GL_SHADER_STORAGE_BUFFER, 0, model.verticesBuffer());
            shader.setUniform("worldPos", model.X(), model.Y(), model.Z(), 1 << model.LOD());

            GL46.glMultiDrawArrays(GL46.GL_TRIANGLES, model.getIndices(), toRenderVertexCounts);
        }
    }

    private static void renderWater(Position playerPosition, Matrix4f projectionViewMatrix, Player player) {
        Shader shader = AssetManager.getShader(ShaderIdentifier.WATER);
        shader.bind();
        shader.setUniform("projectionViewMatrix", projectionViewMatrix);
        shader.setUniform("iCameraPosition",
                playerPosition.intPosition().x & ~CHUNK_SIZE_MASK,
                playerPosition.intPosition().y & ~CHUNK_SIZE_MASK,
                playerPosition.intPosition().z & ~CHUNK_SIZE_MASK);
        shader.setUniform("cameraPosition", playerPosition.getInChunkPosition());
        shader.setUniform("textureAtlas", 0);

        GL46.glBindVertexArray(AssetManager.getVertexArray(VertexArrayIdentifier.SKYBOX).getID()); // Just bind something IDK
        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glDisable(GL46.GL_CULL_FACE);
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
        GL46.glActiveTexture(GL46.GL_TEXTURE0);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, AssetManager.getTexture(TextureIdentifier.MATERIALS).getID());

        for (TransparentModel model : player.getMeshCollector().getTransparentModels(0)) {
            if (model == null || !model.containsWater()) continue;

            GL46.glBindBufferBase(GL46.GL_SHADER_STORAGE_BUFFER, 0, model.verticesBuffer());
            shader.setUniform("worldPos", model.X(), model.Y(), model.Z(), 1 << model.LOD());

            GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, model.waterVertexCount() * (6 / 2));
        }
    }


    private void renderDebugInfo() {
        long currentTime = System.nanoTime();
        frameTimes.removeIf(frameTime -> currentTime - frameTime > 1_000_000_000L);
        frameTimes.add(currentTime);

        Player player = Game.getPlayer();
        Position playerPosition = player.getPosition();
        Vector3f direction = player.getCamera().getDirection();
        Vector3f rotation = player.getCamera().getRotation();
        Chunk chunk = Game.getWorld().getChunk(
                playerPosition.intPosition().x >> CHUNK_SIZE_BITS,
                playerPosition.intPosition().y >> CHUNK_SIZE_BITS,
                playerPosition.intPosition().z >> CHUNK_SIZE_BITS, 0);

        int textLine = 0;
        renderTextLine("FPS: %s".formatted(frameTimes.size()), Color.RED, ++textLine);

        renderTextLine("Position %s, Fraction %s".formatted(playerPosition.intPositionToString(), playerPosition.fractionToString()), Color.WHITE, ++textLine);
        renderTextLine("Chunk Position %s, In Chunk Position %s".formatted(playerPosition.chunkCoordinateToString(), playerPosition.inChunkPositionToString()), Color.WHITE, ++textLine);
        renderTextLine("Direction X:%s, Y:%s, Z:%s".formatted(direction.x, direction.y, direction.z), Color.WHITE, ++textLine);
        renderTextLine("Rotation Pitch:%s, Yaw:%s, Roll:%s".formatted(rotation.x, rotation.y, rotation.z), Color.GRAY, ++textLine);

        renderTextLine("Seed: %s".formatted(WorldGeneration.SEED), Color.GRAY, ++textLine);

        if (chunk == null) renderTextLine("The current Chunk is null :[", Color.GREEN, ++textLine);
        else {
            renderTextLine("Current Chunk generated:%s, meshed:%s".formatted(chunk.isGenerated(), player.getMeshCollector().isMeshed(chunk.INDEX, 0)), Color.GREEN, ++textLine);
            renderTextLine("Chunk Coordinate [X:%s, Y:%s, Z:%s]".formatted(chunk.X, chunk.Y, chunk.Z), Color.GREEN, ++textLine);
            renderTextLine("Chunk Index:%s, Chunk ID:%s".formatted(chunk.INDEX, chunk.ID), Color.GREEN, ++textLine);
        }
    }

    private static void renderTextLine(String text, Color color, int textLine) {
        TextShader shader = (TextShader) AssetManager.getShader(ShaderIdentifier.TEXT);
        shader.bind();

        float lineSeparation = TextElement.DEFAULT_TEXT_SIZE.y * FloatSetting.TEXT_SIZE.value();
        Vector2f position = new Vector2f(0.0f, 1.0f - textLine * lineSeparation);

        shader.drawText(position, text, color, true, false);
    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }

    private boolean debugScreenOpen = false;
    private final ArrayList<Long> frameTimes = new ArrayList<>();
}
