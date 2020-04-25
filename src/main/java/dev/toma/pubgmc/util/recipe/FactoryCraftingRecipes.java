package dev.toma.pubgmc.util.recipe;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FactoryCraftingRecipes extends JsonReloadListener {

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(DeserializationOutput.class, new DeserializationOutput.Deserializer())
            .registerTypeAdapter(PMCRecipe.class, new PMCRecipe.RecipeDeserializer())
            .registerTypeAdapter(ItemStack.class, new PMCRecipe.StackDeserializer())
            .registerTypeAdapter(new TypeToken<List<ItemStack>>(){}.getType(), new PMCRecipe.ListDeserializer())
            .create();
    public Map<String, List<PMCRecipe>> recipeMap = new HashMap<>();

    public FactoryCraftingRecipes() {
        super(gson, "factory");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> objectMap, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        for(Map.Entry<ResourceLocation, JsonObject> entry : objectMap.entrySet()) {
            try {
                DeserializationOutput output = gson.fromJson(entry.getValue(), DeserializationOutput.class);
                if(!recipeMap.containsKey(output.factory)) {
                    recipeMap.put(output.factory, new ArrayList<>());
                    Pubgmc.pubgmcLog.info("Created new factory category with key {}", output.factory);
                }
                recipeMap.get(output.factory).add(output.recipe);
            } catch (JsonParseException e) {
                Pubgmc.pubgmcLog.error("Invalid factory recipe {}: {}", entry.getKey(), e.getMessage());
            }
        }
        Pubgmc.pubgmcLog.info("Registered {} recipe categories with total of {} recipes", recipeMap.size(), UsefulFunctions.getElementCount(recipeMap));
    }

    private static class DeserializationOutput {

        private String factory;
        private PMCRecipe recipe;

        private DeserializationOutput(String factory, PMCRecipe recipe) {
            this.factory = factory;
            this.recipe = recipe;
        }

        private static class Deserializer implements JsonDeserializer<DeserializationOutput> {

            @Override
            public DeserializationOutput deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if(!json.isJsonObject()) throw new JsonParseException("Error parsing recipe, JsonObject type is required");
                JsonObject recipe = json.getAsJsonObject();
                String category = recipe.has("category") ? recipe.getAsJsonPrimitive("category").getAsString() : null;
                if(category == null) throw new JsonParseException("Error parsing recipe, 'category' property is required!");
                JsonObject object = recipe.has("recipe") ? recipe.getAsJsonObject("recipe") : null;
                if(object == null) throw new JsonParseException("Error parsing recipe, 'recipe' property is required!");
                PMCRecipe impl = context.deserialize(object, PMCRecipe.class);
                return new DeserializationOutput(category, impl);
            }
        }
    }
}
