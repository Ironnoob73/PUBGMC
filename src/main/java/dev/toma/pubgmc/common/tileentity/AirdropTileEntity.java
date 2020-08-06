package dev.toma.pubgmc.common.tileentity;

import dev.toma.pubgmc.common.container.AirdropContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static dev.toma.pubgmc.init.PMCTileEntities.AIRDROP;

public class AirdropTileEntity extends AbstractInventoryTileEntity {

    public AirdropTileEntity() {
        super(AIRDROP.get());
    }

    @Override
    public IItemHandler createInventory() {
        return new ItemStackHandler(9);
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new AirdropContainer(p_createMenu_1_, p_createMenu_2_, this);
    }
}
