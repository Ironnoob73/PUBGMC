package dev.toma.pubgmc.common.tileentity;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.LootSpawnerContainer;
import dev.toma.pubgmc.data.loot.LootManager;
import dev.toma.pubgmc.data.loot.LootTable;
import dev.toma.pubgmc.data.loot.LootTableConstants;
import dev.toma.pubgmc.games.interfaces.LootGenerator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import java.util.List;

import static dev.toma.pubgmc.init.PMCTileEntities.LOOT_SPAWNER;

public class LootSpawnerTileEntity extends AbstractInventoryTileEntity implements LootGenerator {

    private long id = -1;

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

    @Override
    public void generateLoot() {
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
                List<ItemStack> stacks = table.getLoot(world.rand, true);
                for(ItemStack stack : stacks) {
                    inv.insertItem(at, stack, false);
                    ++at;
                    at = postItemGenerated(stack, at, inventory);
                }
            }
        });
    }

    @Override
    public void setGameID(long ID) {
        this.id = ID;
    }

    @Override
    public long getGameID() {
        return id;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putLong("gameID", id);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        id = compound.getLong("gameID");
    }
}
