package dev.toma.pubgmc.common.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import static dev.toma.pubgmc.Registry.PMCTileEntities.WEAPON_FACTORY;

public class TileEntityWeaponFactory extends InventoryTileEntity {

    public TileEntityWeaponFactory() {
        super(WEAPON_FACTORY);
    }

    @Override
    public NonNullList<ItemStack> createInventory() {
        return NonNullList.withSize(9, ItemStack.EMPTY);
    }
}
