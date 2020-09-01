package dev.toma.pubgmc.data.loot;

import com.google.gson.*;
import dev.toma.pubgmc.util.JsonHelper;
import dev.toma.pubgmc.util.object.WeightedRandom;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class LootTable {

    private final WeightedRandom<LootPool> weightedRandom;
    private final List<LootPool> forced;

    public LootTable(LootPool[] lootPools, @Nullable List<LootPool> forcedPools) {
        this.weightedRandom = new WeightedRandom<>(LootPool::getWeight, lootPools);
        this.forced = forcedPools;
    }

    public boolean isEmpty() {
        return weightedRandom.getEntries().length == 0;
    }

    public ItemStack getRandom(Random random) {
        return weightedRandom.get().pickRandom().get(random);
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
            //List<LootPool> forcedPools =
            return new LootTable(pools);
        }
    }
}
