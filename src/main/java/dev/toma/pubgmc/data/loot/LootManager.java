package dev.toma.pubgmc.data.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.item.gun.AmmoType;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.Map;

public class LootManager extends JsonReloadListener {

    private static final Map<ResourceLocation, LootTable> LOOT_TABLES = new HashMap<>();
    public static final Marker marker = MarkerManager.getMarker("LootManager");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LootTable.class, new LootTable.Deserializer())
            .registerTypeAdapter(LootEntry.class, new LootEntry.Deserializer())
            .registerTypeAdapter(LootPool.class, new LootPool.Deserializer())
            .create();
    private static final LootTable EMPTY = new LootTable(new LootPool[0], null);

    public LootManager() {
        super(GSON, "loot");
    }

    public static LootTable getLootTable(ResourceLocation location) {
        LootTable table = LOOT_TABLES.get(location);
        return table != null ? table : EMPTY;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> resourceLocationJsonObjectMap, IResourceManager iResourceManager, IProfiler iProfiler) {
        LOOT_TABLES.clear();
        Pubgmc.pubgmcLog.debug(marker, "Loading loot configurations");
        for(Map.Entry<ResourceLocation, JsonObject> entry : resourceLocationJsonObjectMap.entrySet()) {
            try {
                LOOT_TABLES.put(entry.getKey(), GSON.fromJson(entry.getValue(), LootTable.class));
            } catch (JsonParseException ex) {
                Pubgmc.pubgmcLog.error(marker, "Exception parsing loot file {}: {}", entry.getKey(), ex.getMessage());
            }
        }
        Pubgmc.pubgmcLog.info(marker, "Loaded {} loot configurations", LOOT_TABLES.size());
    }
}
