package dev.toma.pubgmc.data.recipe;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PMCRecipe {

    protected final ItemStack result;
    protected final List<ItemStack> ingredientList;

    public PMCRecipe(ItemStack stack, List<ItemStack> ingredientList) {
        this.result = stack;
        this.ingredientList = ingredientList;
    }

    public ItemStack getRaw() {
        return this.result;
    }

    public ItemStack craft() {
        return this.result.copy();
    }

    public List<ItemStack> ingredientList() {
        return ingredientList;
    }

    public static class RecipeDeserializer implements JsonDeserializer<PMCRecipe> {
        @Override
        public PMCRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            JsonElement e1 = object.has("result") ? object.get("result") : null;
            if(e1 == null || !e1.isJsonObject()) throw new JsonParseException("Error parsing recipe data, missing or invalid property 'result'");
            JsonObject result = e1.getAsJsonObject();
            JsonElement e2 = object.has("ingredients") ? object.get("ingredients") : null;
            if(e2 == null || !e2.isJsonArray()) throw new JsonParseException("Error parsing recipe data, missing or invalid property 'ingredients'");
            JsonArray ingredients = e2.getAsJsonArray();
            if(ingredients.size() == 0) throw new JsonParseException("Error parsing recipe data, ingredient amount must be >0!");
            ItemStack resultStack = context.deserialize(result, ItemStack.class);
            List<ItemStack> ingredientList = context.deserialize(ingredients, new TypeToken<List<ItemStack>>(){}.getType());
            return new PMCRecipe(resultStack, ingredientList);
        }
    }

    public static class StackDeserializer implements JsonDeserializer<ItemStack> {
        @Override
        public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            JsonElement element = object.has("item") ? object.get("item") : null;
            if(element == null) throw new JsonParseException("Error parsing recipe result data, 'item' property is required!");
            JsonElement c = object.has("count") ? object.get("count") : new JsonPrimitive(1);
            ResourceLocation location = new ResourceLocation(element.getAsString());
            Item item = ForgeRegistries.ITEMS.getValue(location);
            if(item == null || item == Items.AIR) throw new JsonParseException("Unknown item " + location.toString());
            return new ItemStack(item, c.getAsInt());
        }
    }

    public static class ListDeserializer implements JsonDeserializer<List<ItemStack>> {
        @Override
        public List<ItemStack> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<ItemStack> list = new ArrayList<>();
            JsonArray array = json.getAsJsonArray();
            for(JsonElement element : array) {
                if(!element.isJsonObject()) throw new JsonParseException("Invalid json type in ingredient list!");
                list.add(context.deserialize(element, ItemStack.class));
            }
            return list;
        }
    }
}
