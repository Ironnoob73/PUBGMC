package dev.toma.pubgmc.data.loot;

import com.google.gson.*;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.util.JsonHelper;
import dev.toma.pubgmc.util.object.LazyLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.JsonUtils;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LootEntry {

    public static final LazyLoader<LootEntry> EMPTY = new LazyLoader<>(() -> new LootEntry(new Item[] {Items.AIR}));
    private final Item[] item;
    private final CompoundNBT nbt;
    private final int baseCount;
    private final int randomCount;

    public LootEntry(Item[] item) {
        this(item, 1);
    }

    public LootEntry(Item[] item, int baseCount) {
        this(item, baseCount, 0, null);
    }

    public LootEntry(Item[] item, int baseCount, int randomCount, CompoundNBT nbt) {
        this.item = item;
        this.baseCount = baseCount;
        this.randomCount = randomCount;
        this.nbt = nbt;
    }

    public ItemStack get(Random random) {
        ItemStack stack = new ItemStack(item[random.nextInt(item.length)], baseCount + Pubgmc.rand.nextInt(randomCount + 1));
        if(nbt != null) {
            stack.setTag(nbt);
        }
        return stack;
    }

    public static class Deserializer implements JsonDeserializer<LootEntry> {

        @Override
        public LootEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = (JsonObject) json;
            JsonElement element = JsonHelper.get("item", object, (k, o) -> o.get(k), () -> new JsonParseException("Entry item/s are not defined!"));
            Item[] items = null;
            if(element.isJsonArray()) {
                List<ResourceLocation> list = new ArrayList<>();
                for(JsonElement e : element.getAsJsonArray()) {
                    list.add(new ResourceLocation(e.getAsString()));
                }
                Iterator<ResourceLocation> it = list.iterator();
                while (it.hasNext()) {
                    ResourceLocation resourceLocation = it.next();
                    Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
                    if(item == null) {
                        Pubgmc.pubgmcLog.error(LootManager.marker, "Unknown item: " + resourceLocation.toString());
                        it.remove();
                    }
                }
                if(list.isEmpty()) {
                    throw new JsonParseException("No items defined!");
                }
                items = new Item[list.size()];
                int i = 0;
                for(ResourceLocation rl : list) {
                    items[i] = ForgeRegistries.ITEMS.getValue(rl);
                    ++i;
                }
            } else if(element.isJsonPrimitive()) {
                ResourceLocation location = new ResourceLocation(JsonHelper.getString("item", object, () -> new JsonParseException("Entry item is not defined!")));
                Item item = ForgeRegistries.ITEMS.getValue(location);
                if(item == null) throw new JsonParseException("Unknown item: " + location.toString());
                items = new Item[1];
                items[0] = item;
            } else throw new JsonParseException("Unknown type of 'item' property. Expected type string or array");
            int amount = object.has("count") ? MathHelper.clamp(object.get("count").getAsInt(), 1, 64) : 1;
            int extra = object.has("random") ? MathHelper.clamp(object.get("random").getAsInt(), 0, 64 - amount) : 0;
            CompoundNBT nbt = object.has("nbt") ? JsonUtils.readNBT(object, "nbt") : null;
            return new LootEntry(items, amount, extra, nbt);
        }
    }
}
