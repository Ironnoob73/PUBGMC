package dev.toma.pubgmc.data.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class LootManager extends JsonReloadListener {

    public static final Map<ResourceLocation, LootTable> LOOT_TABLES = new HashMap<>();
    private static final Logger log = LogManager.getLogger("pubgmc/LootManager");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LootTable.class, new LootTable.Deserializer())
            .registerTypeAdapter(LootEntry.class, new LootEntry.Deserializer())
            .registerTypeAdapter(LootPool.class, new LootPool.Deserializer())
            .create();

    public LootManager() {
        super(GSON, "loot");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> resourceLocationJsonObjectMap, IResourceManager iResourceManager, IProfiler iProfiler) {
        LOOT_TABLES.clear();
        log.debug("Loading loot configurations");
        for(Map.Entry<ResourceLocation, JsonObject> entry : resourceLocationJsonObjectMap.entrySet()) {
            try {
                LOOT_TABLES.put(entry.getKey(), GSON.fromJson(entry.getValue(), LootTable.class));
            } catch (JsonParseException ex) {
                log.error("Exception parsing loot file {}: {}", entry.getKey(), ex.getMessage());
            }
        }
        log.info("Loaded {} loot configurations", LOOT_TABLES.size());
    }
}
