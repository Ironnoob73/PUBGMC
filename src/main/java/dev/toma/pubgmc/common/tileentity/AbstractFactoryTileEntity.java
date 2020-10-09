package dev.toma.pubgmc.common.tileentity;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.FactoryContainer;
import dev.toma.pubgmc.init.PMCTileEntities;
import dev.toma.pubgmc.data.recipe.PMCRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractFactoryTileEntity extends AbstractInventoryTileEntity {

    public AbstractFactoryTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public abstract List<PMCRecipe> getRecipeList();

    @Override
    public IItemHandler createInventory() {
        return new ItemStackHandler(9);
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new FactoryContainer(p_createMenu_1_, p_createMenu_2_, this);
    }

    public static class Weapon extends AbstractFactoryTileEntity {

        public Weapon() {
            super(PMCTileEntities.WEAPON_FACTORY.get());
        }

        @Override
        public List<PMCRecipe> getRecipeList() {
            return Pubgmc.getRecipeManager().getWeaponRecipes();
        }
    }

    public static class Ammo extends AbstractFactoryTileEntity {

        public Ammo() {
            super(PMCTileEntities.AMMO_FACTORY.get());
        }

        @Override
        public List<PMCRecipe> getRecipeList() {
            return Pubgmc.getRecipeManager().getAmmoRecipes();
        }
    }
}
