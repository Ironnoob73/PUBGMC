package dev.toma.pubgmc.data.loot;

import com.google.gson.*;
import dev.toma.pubgmc.util.JsonHelper;
import dev.toma.pubgmc.util.object.WeightedRandom;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Type;

public class LootTable {

    private final WeightedRandom<LootPool> weightedRandom;

    public LootTable(LootPool[] lootPools) {
        this.weightedRandom = new WeightedRandom<>(LootPool::getWeight, lootPools);
    }

    public ItemStack getRandom() {
        return weightedRandom.get().pickRandom().get();
    }

    public static class Deserializer implements JsonDeserializer<LootTable> {

        @Override
        public LootTable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!json.isJsonObject()) throw new JsonParseException("Loot table is not json object!");
            JsonObject object = (JsonObject) json;
            JsonArray contentArray = JsonHelper.getArray("content", object, () -> new JsonParseException("Undefined loot content!"));
            JsonObject poolDefs = JsonHelper.getObject("pools", object, () -> new JsonParseException("Undefined loot pools!"));
            int size = contentArray.size();
            LootPool[] pools = new LootPool[size];
            for(int i = 0; i < size; i++) {
                JsonObject contentObj = contentArray.get(i).getAsJsonObject();
                String path = JsonHelper.getString("pool", contentObj, () -> new JsonParseException("Undefined pool path!"));
                JsonArray poolContents = JsonHelper.getArray(path, poolDefs, () -> new JsonParseException(String.format("No pool defined for key %s", path)));
                int weight = contentObj.has("weight") ? contentObj.getAsJsonPrimitive("weight").getAsInt() : 10;
                LootPool pool = context.deserialize(poolContents, LootPool.class);
                pool.withWeight(weight);
                pools[i] = pool;
            }
            return new LootTable(pools);
        }
    }
}
