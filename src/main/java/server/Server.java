package server;

import org.joml.Vector3i;
import player.rendering.MeshCollector;
import utils.Position;
import utils.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static utils.Constants.*;

public final class Server {

    public float getCurrentGameTickFraction() {
        long currentGameTickDuration = System.nanoTime() - gameTickStartTime;
        return (float) ((double) currentGameTickDuration / NANOSECONDS_PER_GAME_TICK);
    }


    public void pauseTicks() {
        if (executor != null) executor.shutdownNow();
    }

    public void startTicks() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::executeGameTickCatchException, 0, NANOSECONDS_PER_GAME_TICK, TimeUnit.NANOSECONDS);

        generator.restart();
    }

    void cleanUp() {
        generator.cleanUp();
    }

    void unloadDistantChunks(Vector3i playerChunkPosition) {
        MeshCollector meshCollector = Game.getPlayer().getMeshCollector();

        for (int lod = 0; lod < LOD_COUNT; lod++) {
            int lodPlayerX = playerChunkPosition.x >> lod;
            int lodPlayerY = playerChunkPosition.y >> lod;
            int lodPlayerZ = playerChunkPosition.z >> lod;

            for (Chunk chunk : Game.getWorld().getLod(lod)) {
                if (chunk == null) continue;

                if (Utils.insideOfPlayerVisibility(lodPlayerX, lodPlayerY, lodPlayerZ, chunk.X, chunk.Y, chunk.Z))
                    continue;

                Game.getWorld().setNull(chunk.INDEX, chunk.LOD);
                meshCollector.removeMesh(chunk.INDEX, chunk.LOD);
            }
        }
    }


    private void executeGameTickCatchException() {
        try {
            gameTickStartTime = System.nanoTime();
            executeGameTick();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void executeGameTick() {
        Position oldPlayerPosition = Game.getPlayer().getPosition();
        Game.getPlayer().updateGameTick();
        Position newPlayerPosition = Game.getPlayer().getPosition();
        if (!oldPlayerPosition.sharesChunkWith(newPlayerPosition)) generator.restart();
    }

    private ScheduledExecutorService executor;
    private final ChunkGenerator generator = new ChunkGenerator();
    private long gameTickStartTime;

    private static final int NANOSECONDS_PER_GAME_TICK = 50_000_000;
}
