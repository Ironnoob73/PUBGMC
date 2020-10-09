package dev.toma.pubgmc.data.deserializers;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.JsonUtils;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;

public class ItemStackDeserializer implements JsonDeserializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(!json.isJsonObject()) {
            throw new JsonSyntaxException("Not a JsonObject!");
        }
        JsonObject object = json.getAsJsonObject();
        ResourceLocation resourceLocation = new ResourceLocation(JSONUtils.getString(object, "item"));
        Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
        if(item == null) {
            throw new JsonSyntaxException("Unknown item: " + resourceLocation.toString());
        }
        int count = JSONUtils.getInt(object, "count", 1);
        ItemStack stack = new ItemStack(item, count);
        CompoundNBT nbt = object.has("nbt") ? JsonUtils.readNBT(object, "nbt") : null;
        if(nbt != null) {
            stack.setTag(nbt);
        }
        return stack;
    }
}
