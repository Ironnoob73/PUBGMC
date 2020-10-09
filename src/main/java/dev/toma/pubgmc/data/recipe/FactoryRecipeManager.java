package dev.toma.pubgmc.data.recipe;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.lang.reflect.Type;
import java.util.*;

public class FactoryRecipeManager extends JsonReloadListener {

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(DeserializationOutput.class, new DeserializationOutput.Deserializer())
            .registerTypeAdapter(PMCRecipe.class, new PMCRecipe.RecipeDeserializer())
            .registerTypeAdapter(ItemStack.class, new PMCRecipe.StackDeserializer())
            .registerTypeAdapter(new TypeToken<List<ItemStack>>(){}.getType(), new PMCRecipe.ListDeserializer())
            .create();
    public Map<RecipeType, List<PMCRecipe>> recipeMap = new HashMap<>();
    private static final Marker MARKER = MarkerManager.getMarker("FactoryCraftingRecipes");

    public FactoryRecipeManager() {
        super(gson, "factory");
    }

    public List<PMCRecipe> getWeaponRecipes() {
        return UsefulFunctions.getNonnullFromMap(recipeMap, RecipeType.WEAPON, Collections.emptyList());
    }

    public List<PMCRecipe> getAmmoRecipes() {
        return UsefulFunctions.getNonnullFromMap(recipeMap, RecipeType.AMMO, Collections.emptyList());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> objectMap, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        recipeMap = new HashMap<>();
        for(Map.Entry<ResourceLocation, JsonObject> entry : objectMap.entrySet()) {
            try {
                DeserializationOutput output = gson.fromJson(entry.getValue(), DeserializationOutput.class);
                recipeMap.computeIfAbsent(output.factory, t -> new ArrayList<>()).add(output.recipe);
            } catch (JsonParseException e) {
                Pubgmc.pubgmcLog.error(MARKER, "Invalid factory recipe file {}: {}", entry.getKey(), e.getMessage());
            }
        }
        Pubgmc.pubgmcLog.info(MARKER, "Registered {} recipe categories with total of {} recipes", recipeMap.size(), UsefulFunctions.getElementCount(recipeMap));
    }

    private static class DeserializationOutput {

        private final RecipeType factory;
        private final PMCRecipe recipe;

        private DeserializationOutput(RecipeType factory, PMCRecipe recipe) {
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
                RecipeType type = RecipeType.valueOf(category.toUpperCase());
                JsonObject object = recipe.has("recipe") ? recipe.getAsJsonObject("recipe") : null;
                if(object == null) throw new JsonParseException("Error parsing recipe, 'recipe' property is required!");
                PMCRecipe impl = context.deserialize(object, PMCRecipe.class);
                return new DeserializationOutput(type, impl);
            }
        }
    }
}
