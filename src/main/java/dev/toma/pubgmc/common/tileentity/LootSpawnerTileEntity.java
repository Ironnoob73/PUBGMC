package dev.toma.pubgmc.common.tileentity;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.LootSpawnerContainer;
import dev.toma.pubgmc.common.item.gun.AmmoType;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.data.loot.LootManager;
import dev.toma.pubgmc.data.loot.LootTable;
import dev.toma.pubgmc.data.loot.LootTableConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static dev.toma.pubgmc.init.PMCTileEntities.LOOT_SPAWNER;

public class LootSpawnerTileEntity extends AbstractInventoryTileEntity {

    public LootSpawnerTileEntity() {
        super(LOOT_SPAWNER.get());
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new LootSpawnerContainer(windowID, playerInventory, this);
    }

    @Override
    public ItemStackHandler createInventory() {
        return new ItemStackHandler(9);
    }

    public void genLoot() {
        this.genLoot(LootManager.getLootTable(LootTableConstants.LOOT_BLOCK));
    }

    public void genLoot(LootTable table) {
        clearInventory();
        if(table.isEmpty()) return;
        inventory.ifPresent(inv -> {
            int attempts = 1 + Pubgmc.rand.nextInt(5);
            int at = 0;
            for(int n = 0; n < attempts; n++) {
                if(at >= inv.getSlots()) break;
                ItemStack stack = table.getRandom();
                inv.insertItem(at, stack, false);
                ++at;
                at = postStackGen(stack, at);
            }
        });
    }

    private int postStackGen(ItemStack stack, int atIndex) {
        if(stack.getItem() instanceof GunItem && inventory.isPresent()) {
            IItemHandler handler = inventory.orElseThrow(NullPointerException::new);
            GunItem gun = (GunItem) stack.getItem();
            AmmoType ammoType = gun.getAmmoType();
            int ammoBoxes = 1 + Pubgmc.rand.nextInt(3);
            for(int a = 0; a < ammoBoxes; a++) {
                if(atIndex >= handler.getSlots()) break;
                handler.insertItem(atIndex, ammoType.generateAmmoDrop(), false);
                ++atIndex;
            }
        }
        return atIndex;
    }
}
