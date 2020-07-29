package dev.toma.pubgmc.data.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.pubgmc.Pubgmc;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class LootListener extends JsonReloadListener {

    public static final Map<ResourceLocation, LootTable> LOOT_TABLES = new HashMap<>();
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LootTable.class, new LootTable.Deserializer())
            .registerTypeAdapter(LootEntry.class, new LootEntry.Deserializer())
            .registerTypeAdapter(LootPool.class, new LootPool.Deserializer())
            .create();

    public LootListener() {
        super(GSON, "loot");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> resourceLocationJsonObjectMap, IResourceManager iResourceManager, IProfiler iProfiler) {
        LOOT_TABLES.clear();
        for(Map.Entry<ResourceLocation, JsonObject> entry : resourceLocationJsonObjectMap.entrySet()) {
            try {
                LOOT_TABLES.put(entry.getKey(), GSON.fromJson(entry.getValue(), LootTable.class));
            } catch (JsonParseException ex) {
                Pubgmc.pubgmcLog.error("Exception parsing loot file {}: {}", entry.getKey(), ex.getMessage());
            }
        }
    }
}
