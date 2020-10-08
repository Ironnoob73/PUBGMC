package dev.toma.pubgmc.integration.jei;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.init.PMCBlocks;
import dev.toma.pubgmc.util.recipe.PMCRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JeiCompatMain implements IModPlugin {

    private IRecipeCategory<PMCRecipe> weaponCategory;
    private IRecipeCategory<PMCRecipe> ammoCategory;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        weaponCategory = new WeaponFactoryRecipeCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(weaponCategory);
        ammoCategory = new AmmoFactoryRecipeCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(ammoCategory);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(Pubgmc.getRecipeManager().getWeaponRecipes(), weaponCategory.getUid());
        registration.addRecipes(Pubgmc.getRecipeManager().getAmmoRecipes(), ammoCategory.getUid());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(PMCBlocks.WEAPON_FACTORY), weaponCategory.getUid());
        registration.addRecipeCatalyst(new ItemStack(PMCBlocks.AMMO_FACTORY), ammoCategory.getUid());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return Pubgmc.makeResource("jei");
    }
}
