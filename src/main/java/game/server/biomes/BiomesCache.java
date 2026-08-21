package game.server.biomes;

import static game.utils.Constants.*;
import static game.assets.StructureCollectionIdentifier.*;

public final class BiomesCache {

    public static final Biome
            BEACH = new Beach(),
            WASTELAND = new Wasteland(),
            CORRODED_MESA = new CorrodedMesa(),
            OCEAN = new Ocean(),
            WARM_OCEAN = new WarmOcean(),
            COLD_OCEAN = new ColdOcean(),
            MOUNTAIN = new Mountain(),
            DRY_MOUNTAIN = new DryMountain(),
            SNOWY_MOUNTAIN = new SnowyMountain(),
            REDWOOD_FOREST = new RedwoodForest(),
            PLAINS = new LayeredSurfaceBiome("Plains", OAK_TREES, 32, 8, 48, GRASS, DIRT),
            SNOWY_PLAINS = new HomogenousSurfaceBiome("Snowy Plains", SPRUCE_TREES, 32, 48, SNOW),
            BLACK_WOOD_FOREST = new LayeredSurfaceBiome("Black Wood Forest", BLACK_WOOD_TREES, 128, 8, 48, PODZOL, DIRT),
            DARK_OAK_FOREST = new LayeredSurfaceBiome("Dark Oak Forest", DARK_OAK_TREES, 128, 8, 48, PODZOL, DIRT),
            OAK_FOREST = new LayeredSurfaceBiome("Oak Forest", OAK_TREES, 128, 8, 48, GRASS, DIRT),
            PINE_FOREST = new LayeredSurfaceBiome("Pine Forest", PINE_TREES, 128, 8, 48, GRASS, DIRT),
            SNOWY_SPRUCE_FOREST = new HomogenousSurfaceBiome("Snowy Spruce Forest", SPRUCE_TREES, 128, 48, SNOW),
            SPRUCE_FOREST = new LayeredSurfaceBiome("Spruce Forest", SPRUCE_TREES, 128, 8, 48, GRASS, DIRT),
            MESA = new LayeredSurfaceBiome("Mesa", null, 0, 48, 128, RED_SAND, RED_SANDSTONE),
            DESERT = new LayeredSurfaceBiome("Desert", null, 0, 48, 128, SAND, SANDSTONE);
}
