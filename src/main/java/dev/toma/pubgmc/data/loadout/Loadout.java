package dev.toma.pubgmc.data.loadout;

import com.google.gson.*;
import dev.toma.pubgmc.Pubgmc;
import net.minecraft.util.JSONUtils;
import net.minecraft.world.World;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Loadout {

    private List<LoadoutEntry> entryList;

    public Loadout(List<LoadoutEntry> entryList) {
        this.entryList = entryList;
    }

    public LoadoutEntry getRandom(World world) {
        return entryList.get(world.rand.nextInt(entryList.size()));
    }

    public static class Deserializer implements JsonDeserializer<Loadout> {

        @Override
        public Loadout deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!json.isJsonObject()) {
                throw new JsonSyntaxException("Loadout must be JsonObject!");
            }
            JsonObject object = json.getAsJsonObject();
            String type = JSONUtils.getString(object, "type");
            LoadoutManager manager = Pubgmc.getLoadoutManager();
            BiFunction<JsonObject, JsonDeserializationContext, ? extends LoadoutEntry> deserializer = manager.getDeserializer(type);
            if(deserializer == null) {
                throw new JsonSyntaxException("Unknown loadout type: " + type);
            }
            JsonArray array = JSONUtils.getJsonArray(object, "entries");
            if(array.size() == 0) {
                throw new JsonSyntaxException("Entry count must be >0!");
            }
            List<LoadoutEntry> loadoutEntries = new ArrayList<>();
            for (JsonElement element : array) {
                if(!element.isJsonObject()) {
                    throw new JsonSyntaxException("Entry must be JsonObject!");
                }
                JsonObject entryObject = element.getAsJsonObject();
                LoadoutEntry entry = deserializer.apply(entryObject, context);
                loadoutEntries.add(entry);
            }
            return new Loadout(loadoutEntries);
        }
    }
}
