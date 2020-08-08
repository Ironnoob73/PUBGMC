package dev.toma.pubgmc.data.loot;

import com.google.gson.*;
import dev.toma.pubgmc.Pubgmc;

import java.lang.reflect.Type;

public class LootPool {

    private int weight;
    private final LootEntry[] lootEntries;

    public LootPool(LootEntry[] entries) {
        this.weight = 10;
        this.lootEntries = entries;
    }

    public void withWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public LootEntry pickRandom() {
        return lootEntries.length == 0 ? LootEntry.EMPTY.get() : lootEntries[Pubgmc.rand.nextInt(lootEntries.length)];
    }

    public static class Deserializer implements JsonDeserializer<LootPool> {

        @Override
        public LootPool deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray array = (JsonArray) json;
            LootEntry[] entries = new LootEntry[array.size()];
            for(int i = 0; i < array.size(); i++) {
                JsonObject object = array.get(i).getAsJsonObject();
                entries[i] = context.deserialize(object, LootEntry.class);
            }
            return new LootPool(entries);
        }
    }
}
