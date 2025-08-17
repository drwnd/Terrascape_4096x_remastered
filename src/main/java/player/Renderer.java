package player;

import assets.AssetManager;
import assets.identifiers.ShaderIdentifier;
import assets.identifiers.TextureIdentifier;
import assets.identifiers.VertexArrayIdentifier;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL46;
import renderables.Renderable;
import rendering_api.shaders.Shader;
import server.GameHandler;
import utils.Transformation;
import utils.Utils;

import static utils.Constants.*;

public final class Renderer extends Renderable {
    public Renderer() {
        super(new Vector2f(1.0f, 1.0f), new Vector2f(0.0f, 0.0f));
        allowScaling(false);
    }

    @Override
    protected void renderSelf(Vector2f position, Vector2f size) {
        Player player = GameHandler.getPlayer();
        Camera camera = player.getCamera();
        camera.updateProjectionMatrix();
        Matrix4f projectionViewMatrix = Transformation.getProjectionViewMatrix(camera);
        Position playerPosition = player.getToRenderPosition();
        player.updateFrame();

        renderSkybox(camera);
        renderOpaqueGeometry(playerPosition, projectionViewMatrix, player);
        renderDebugInfo();
    }

    private static void renderSkybox(Camera camera) {
        GL46.glDisable(GL46.GL_BLEND);
        Shader shader = AssetManager.getShader(ShaderIdentifier.SKYBOX);

        shader.bind();
        shader.setUniform("textureAtlas1", 0);
        shader.setUniform("textureAtlas2", 1);
        shader.setUniform("time", 1.0f);
        shader.setUniform("projectionViewMatrix", Transformation.createSkyBoxTransformationMatrix(camera));

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

        Shader shader = AssetManager.getShader(ShaderIdentifier.OPAQUE_GEOMETRY);
        shader.bind();
        shader.setUniform("projectionViewMatrix", projectionViewMatrix);
        shader.setUniform("iCameraPosition",
                Utils.floor(playerPosition.intPosition().x) & ~CHUNK_SIZE_MASK,
                Utils.floor(playerPosition.intPosition().y) & ~CHUNK_SIZE_MASK,
                Utils.floor(playerPosition.intPosition().z) & ~CHUNK_SIZE_MASK);
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
            int[] toRenderVertexCounts = model.getVertexCounts(playerChunkX, playerChunkY, playerChunkZ);

            GL46.glBindBufferBase(GL46.GL_SHADER_STORAGE_BUFFER, 0, model.verticesBuffer());
            shader.setUniform("worldPos", model.X(), model.Y(), model.Z(), 1 << model.LOD());

            GL46.glMultiDrawArrays(GL46.GL_TRIANGLES, model.getIndices(), toRenderVertexCounts);
        }
    }

    private static void renderDebugInfo() {

    }

    @Override
    protected void resizeSelfTo(int width, int height) {

    }
}
