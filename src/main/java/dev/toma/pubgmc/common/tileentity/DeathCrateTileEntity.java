package dev.toma.pubgmc.common.tileentity;

import dev.toma.pubgmc.common.container.DeathCrateContainer;
import dev.toma.pubgmc.init.PMCTileEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class DeathCrateTileEntity extends AbstractInventoryTileEntity {

    public DeathCrateTileEntity() {
        super(PMCTileEntities.DEATH_CRATE.get());
    }

    @Override
    public IItemHandler createInventory() {
        return new ItemStackHandler(45);
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new DeathCrateContainer(p_createMenu_1_, p_createMenu_2_, this);
    }
}
