package game.server.biomes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import core.assets.Asset;
import core.assets.AssetManager;
import core.assets.identifiers.AssetIdentifier;
import core.utils.FileManager;
import core.utils.RuntimeTypeAdapterFactory;

public final class BiomesCache implements Asset {

    public static final AssetIdentifier<BiomesCache> IDENTIFIER = BiomesCache::new;

    public final Biome BEACH = new Beach(), WASTELAND = new Wasteland(), CORRODED_MESA = new CorrodedMesa(),
            OCEAN = new Ocean(), WARM_OCEAN = new WarmOcean(), COLD_OCEAN = new ColdOcean(),
            MOUNTAIN = new Mountain(), DRY_MOUNTAIN = new DryMountain(), SNOWY_MOUNTAIN = new SnowyMountain(),
            PLAINS, SNOWY_PLAINS,
            BLACK_WOOD_FOREST, DARK_OAK_FOREST, OAK_FOREST, PINE_FOREST, SNOWY_SPRUCE_FOREST, SPRUCE_FOREST, REDWOOD_FOREST = new RedwoodForest(),
            MESA, DESERT;

    private BiomesCache() {
        RuntimeTypeAdapterFactory<Biome> factory = RuntimeTypeAdapterFactory.of(Biome.class);
        factory.registerSubtype(HomogenousSurfaceBiome.class, "HomogenousSurface");
        factory.registerSubtype(LayeredSurfaceBiome.class, "LayeredSurface");
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(factory).create();

        PLAINS = load(gson, "Plains");
        SNOWY_PLAINS = load(gson, "SnowyPlains");
        BLACK_WOOD_FOREST = load(gson, "BlackWoodForest");
        DARK_OAK_FOREST = load(gson, "DarkOakForest");
        OAK_FOREST = load(gson, "OakForest");
        PINE_FOREST = load(gson, "PineForest");
        SNOWY_SPRUCE_FOREST = load(gson, "SnowySpruceForest");
        SPRUCE_FOREST = load(gson, "SpruceForest");
        MESA = load(gson, "Mesa");
        DESERT = load(gson, "Desert");
    }

    @Override
    public void delete() {

    }


    private static Biome load(Gson gson, String name) {
        String filepath = AssetManager.getAssetFilepath("biomes/%s.json".formatted(name));
        String json = FileManager.loadJson(filepath);
        Biome biome = gson.fromJson(json, Biome.class);
        if (biome instanceof LayeredSurfaceBiome layeredSurfaceBiome) biome = LayeredSurfaceBiome.withName(name, layeredSurfaceBiome);
        if (biome instanceof HomogenousSurfaceBiome homogenousSurfaceBiome) biome = HomogenousSurfaceBiome.withName(name, homogenousSurfaceBiome);
        return biome;
    }
}
