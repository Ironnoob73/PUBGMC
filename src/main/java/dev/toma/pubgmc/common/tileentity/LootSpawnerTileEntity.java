package dev.toma.pubgmc.common.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import static dev.toma.pubgmc.Registry.PMCTileEntities.LOOT_SPAWNER;

public class LootSpawnerTileEntity extends InventoryTileEntity {

    public LootSpawnerTileEntity() {
        super(LOOT_SPAWNER.get());
    }

    @Override
    public NonNullList<ItemStack> createInventory() {
        return NonNullList.withSize(9, ItemStack.EMPTY);
    }
}
