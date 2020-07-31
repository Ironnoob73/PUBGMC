package dev.toma.pubgmc.common.tileentity;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.item.gun.AmmoType;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.data.loot.LootManager;
import dev.toma.pubgmc.data.loot.LootTable;
import dev.toma.pubgmc.data.loot.LootTableConstants;
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

    public void genLoot(LootTable table) {
        if(table.isEmpty()) return;
        int attempts = 1 + Pubgmc.rand.nextInt(5);
        int at = 0;
        for(int n = 0; n < attempts; n++) {
            if(at >= getSizeInventory()) break;
            ItemStack stack = table.getRandom();
            setInventorySlotContents(at, stack);
            ++at;
            at = postStackGen(stack, at);
        }
    }

    public void genLoot() {
        this.genLoot(LootManager.getLootTable(LootTableConstants.LOOT_BLOCK));
    }

    private int postStackGen(ItemStack stack, int atIndex) {
        if(stack.getItem() instanceof GunItem) {
            GunItem gun = (GunItem) stack.getItem();
            AmmoType ammoType = gun.getAmmoType();
            int ammoBoxes = 1 + Pubgmc.rand.nextInt(3);
            for(int a = 0; a < ammoBoxes; a++) {
                if(atIndex >= getSizeInventory()) break;
                setInventorySlotContents(atIndex, ammoType.generateAmmoDrop());
                ++atIndex;
            }
        }
        return atIndex;
    }
}
