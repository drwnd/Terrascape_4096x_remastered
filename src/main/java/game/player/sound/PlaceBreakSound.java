package game.player.sound;

import core.sound.Sound;
import core.utils.Vector3l;

import game.player.interaction.PlaceMode;
import game.player.interaction.ShapePlaceable;
import game.server.Game;
import game.server.generation.Structure;
import game.server.material.Material;
import game.server.materials_data.MaterialsData;
import game.settings.FloatSettings;
import game.settings.OptionSettings;
import game.utils.Position;

import org.joml.Vector3f;

import static game.utils.Constants.*;

public final class PlaceBreakSound {

    public static void playPlaceSounds(long startX, long startY, long startZ, byte transform, Structure structure) {
        boolean[] presentMaterials = structure.materials().getPresentMaterials();
        Vector3l center = new Vector3l(
                startX + ((long) structure.sizeX(transform) >> 1),
                startY + ((long) structure.sizeX(transform) >> 1),
                startZ + ((long) structure.sizeX(transform) >> 1));
        Position centerPosition = new Position(center, new Vector3f());
        for (int index = 0; index < AMOUNT_OF_MATERIALS; index++) {
            if (!presentMaterials[index]) continue;
            Sound.play3D(Material.getDigSounds((byte) index), FloatSettings.DIG_AUDIO, centerPosition, null);
        }
    }

    public static void playPlaceSounds(long startX, long startY, long startZ, int countX, int countY, int countZ, ShapePlaceable placeable) {
        int lengthX = placeable.getLengthX();
        int lengthY = placeable.getLengthY();
        int lengthZ = placeable.getLengthZ();
        Vector3l center = new Vector3l(
                startX + ((long) countX * lengthX >> 1),
                startY + ((long) countY * lengthY >> 1),
                startZ + ((long) countZ * lengthZ >> 1));

        Sound.play3D(Material.getStepSounds(placeable.getMaterial()), FloatSettings.PLACE_AUDIO, new Position(center, new Vector3f()), null);
    }

    public static void playBreakSounds(long startX, long startY, long startZ, int countX, int countY, int countZ, ShapePlaceable placeable) {
        int lengthX = placeable.getLengthX();
        int lengthY = placeable.getLengthY();
        int lengthZ = placeable.getLengthZ();
        Vector3l center = new Vector3l(
                startX + ((long) countX * lengthX >> 1),
                startY + ((long) countY * lengthY >> 1),
                startZ + ((long) countZ * lengthZ >> 1));
        boolean[] involvedMaterials = new boolean[AMOUNT_OF_MATERIALS];

        for (long x = startX; x != startX + (long) countX * lengthX; x += lengthX)
            for (long y = startY; y != startY + (long) countY * lengthY; y += lengthY)
                for (long z = startZ; z != startZ + (long) countZ * lengthZ; z += lengthZ) findInvolvedMaterials(x, y, z, involvedMaterials, placeable);

        Position centerPosition = new Position(center, new Vector3f());
        for (int index = 0; index < AMOUNT_OF_MATERIALS; index++) {
            if (!involvedMaterials[index]) continue;
            Sound.play3D(Material.getDigSounds((byte) index), FloatSettings.DIG_AUDIO, centerPosition, null);
        }
    }

    private static void findInvolvedMaterials(long startX, long startY, long startZ, boolean[] involvedMaterials, ShapePlaceable placeable) {
        if (OptionSettings.PLACE_MODE.value() == PlaceMode.REPLACE_AIR) return;
        boolean breakHeldOnly = OptionSettings.PLACE_MODE.value() == PlaceMode.BREAK_HELD_ONLY;
        byte heldMaterial = breakHeldOnly ? ((ShapePlaceable) Game.getPlayer().getHeldPlaceable()).getMaterial() : AIR;
        int lengthX = placeable.getLengthX();
        int lengthY = placeable.getLengthY();
        int lengthZ = placeable.getLengthZ();
        long[] bitMap = placeable.getBitMap();
        byte material = placeable.getMaterial();

        for (int xOffset = 0; xOffset < lengthX; xOffset ++)
            for (int yOffset = 0; yOffset < lengthY; yOffset ++)
                for (int zOffset = 0; zOffset < lengthZ; zOffset ++) {

                    int bitMapIndex = MaterialsData.getUncompressedIndex(xOffset, yOffset, zOffset);
                    if ((bitMap[bitMapIndex >> 6] & 1L << bitMapIndex) == 0) continue;
                    byte previousMaterial = Game.getWorld().getMaterial(startX + xOffset, startY + yOffset, startZ + zOffset, 0);
                    if (previousMaterial == AIR || previousMaterial == OUT_OF_WORLD
                            || previousMaterial == material
                            || breakHeldOnly && previousMaterial != heldMaterial) continue;

                    involvedMaterials[previousMaterial & 0xFF] = true;
                }
    }
}
