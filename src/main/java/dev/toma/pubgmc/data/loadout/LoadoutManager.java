package dev.toma.pubgmc.data.loadout;

import com.google.gson.*;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.data.deserializers.ItemStackDeserializer;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class LoadoutManager extends JsonReloadListener {

    public static final Marker MARKER = MarkerManager.getMarker("Loadouts");
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(ItemStack.class, new ItemStackDeserializer()).registerTypeAdapter(Loadout.class, new Loadout.Deserializer()).create();
    public final Map<String, BiFunction<JsonObject, JsonDeserializationContext, ? extends LoadoutEntry>> DESERIALIZER_MAP = new HashMap<>();
    public final Map<ResourceLocation, Loadout> loadoutMap = new HashMap<>();

    public BiFunction<JsonObject, JsonDeserializationContext, ? extends LoadoutEntry> getDeserializer(String key) {
        return DESERIALIZER_MAP.get(key);
    }

    public LoadoutManager() {
        super(GSON, "loadout");
        DESERIALIZER_MAP.put("default", LoadoutEntry.Basic::load);
    }

    public Loadout getLoadout(ResourceLocation resourceLocation) {
        return this.loadoutMap.get(resourceLocation);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> splashList, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        loadoutMap.clear();
        Pubgmc.pubgmcLog.info(MARKER, "Loading loadouts");
        for (Map.Entry<ResourceLocation, JsonObject> entry : splashList.entrySet()) {
            try {
                loadoutMap.put(entry.getKey(), GSON.fromJson(entry.getValue(), Loadout.class));
            } catch (JsonParseException ex) {
                Pubgmc.pubgmcLog.error(MARKER, "Exception parsing loadout file {}: {}", entry.getKey(), ex.getMessage());
            }
        }
        Pubgmc.pubgmcLog.info(MARKER, "Loaded {} loadout configurations", loadoutMap.size());
    }
}
