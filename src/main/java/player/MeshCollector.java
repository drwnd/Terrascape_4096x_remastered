package player;

import org.lwjgl.opengl.GL46;
import rendering_api.ObjectLoader;
import utils.Utils;

import java.util.ArrayList;

import static utils.Constants.*;

public final class MeshCollector {

    public void loadAllMeshes() {
        for (Mesh mesh : meshQueue) {
            int chunkIndex = Utils.getChunkIndex(mesh.chunkX(), mesh.chunkY(), mesh.chunkZ());
            int lod = mesh.lod();

            OpaqueModel oldOpaqueModel = getOpaqueModel(chunkIndex, lod);
            OpaqueModel newModel = ObjectLoader.loadOpaqueModel(mesh.opaqueVertices(), mesh.getWorldCoordinate(), mesh.vertexCounts(), lod);
            setOpaqueModel(newModel, chunkIndex, lod);
            if (oldOpaqueModel != null) GL46.glDeleteBuffers(oldOpaqueModel.verticesBuffer());

            TransparentModel oldTransparentModel = getTransparentModel(chunkIndex, lod);
            TransparentModel newTransparentModel = ObjectLoader.loadTransparentModel(mesh.transparentVertices(), mesh.waterVertexCount(), mesh.glassVertexCount(), mesh.getWorldCoordinate(), lod);
            setTransparentModel(newTransparentModel, chunkIndex, lod);
            if (oldTransparentModel != null) GL46.glDeleteBuffers(oldTransparentModel.verticesBuffer());
        }
        meshQueue.clear();
    }

    public void queueMesh(Mesh mesh) {
        meshQueue.add(mesh);
    }

    public boolean isMeshed(int chunkIndex) {
        return (isMeshed[chunkIndex >> 6] & chunkIndex & 63) != 0;
    }

    public void setMeshed(boolean meshed, int chunkIndex) {
        if (meshed) isMeshed[chunkIndex >> 6] |= 1L << chunkIndex;
        else isMeshed[chunkIndex >> 6] &= ~(1L << chunkIndex);
    }

    public OpaqueModel[] getOpaqueModels(int lod) {
        return opaqueModels;
    }

    public TransparentModel[] getTransparentModels(int lod) {
        return transparentModels;
    }


    private OpaqueModel getOpaqueModel(int chunkIndex, int lod) {
        return opaqueModels[chunkIndex];
    }

    private TransparentModel getTransparentModel(int chunkIndex, int lod) {
        return transparentModels[chunkIndex];
    }

    private void setOpaqueModel(OpaqueModel opaqueModel, int index, int lod) {
        opaqueModels[index] = opaqueModel;
    }

    private void setTransparentModel(TransparentModel transparentModel, int index, int lod) {
        transparentModels[index] = transparentModel;
    }


    private final ArrayList<Mesh> meshQueue = new ArrayList<>();
    private final OpaqueModel[] opaqueModels = new OpaqueModel[RENDERED_WORLD_WIDTH * RENDERED_WORLD_HEIGHT * RENDERED_WORLD_WIDTH];
    private final TransparentModel[] transparentModels = new TransparentModel[RENDERED_WORLD_WIDTH * RENDERED_WORLD_HEIGHT * RENDERED_WORLD_WIDTH];
    private final long[] isMeshed = new long[opaqueModels.length / 64 + 1];
}
