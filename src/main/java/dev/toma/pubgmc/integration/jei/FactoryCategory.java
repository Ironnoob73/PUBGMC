package dev.toma.pubgmc.integration.jei;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.data.recipe.PMCRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class FactoryCategory implements IRecipeCategory<PMCRecipe> {

    static final ResourceLocation GUI_TEXTURE = Pubgmc.makeResource("textures/screen/gui_pubgmc.png");
    final IDrawable background;
    final IDrawable icon;
    final String title;

    public FactoryCategory(IGuiHelper guiHelper, Block block) {
        this.background = guiHelper.drawableBuilder(GUI_TEXTURE, 0, 0, 72, 82).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(block));
        this.title = block.getNameTextComponent().getFormattedText();
    }

    @Override
    public Class<? extends PMCRecipe> getRecipeClass() {
        return PMCRecipe.class;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(PMCRecipe recipe, IIngredients iIngredients) {
        List<List<ItemStack>> ingredientlist = new ArrayList<>();
        for (ItemStack stack : recipe.ingredientList()) {
            ingredientlist.add(Collections.singletonList(stack));
        }
        iIngredients.setInputLists(VanillaTypes.ITEM, ingredientlist);
        iIngredients.setOutput(VanillaTypes.ITEM, recipe.getRaw());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, PMCRecipe recipe, IIngredients iIngredients) {
        int i = 0;
        for (List<ItemStack> list : iIngredients.getInputs(VanillaTypes.ITEM)) {
            iRecipeLayout.getItemStacks().init(i, true, i * 18, i > 3 ? 18 : 0);
            iRecipeLayout.getItemStacks().set(i, list);
            ++i;
        }
        iRecipeLayout.getItemStacks().init(++i, true, 27, 64);
        iRecipeLayout.getItemStacks().set(i, recipe.getRaw());
    }
}
