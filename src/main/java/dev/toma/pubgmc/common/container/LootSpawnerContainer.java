package dev.toma.pubgmc.common.container;

import dev.toma.pubgmc.common.tileentity.LootSpawnerTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import static dev.toma.pubgmc.init.PMCContainers.LOOT_SPAWNER;

public class LootSpawnerContainer extends AbstractModContainer<LootSpawnerTileEntity> {

    public LootSpawnerContainer(int id, PlayerInventory playerInventory, LootSpawnerTileEntity tileEntity) {
        super(LOOT_SPAWNER.get(), id, playerInventory, tileEntity);
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            for(int i = 0; i < 9; i++) {
                addSlot(new SlotItemHandler(h, i, 8 + i * 18, 5));
            }
        });
        addPlayerInventory(playerInventory, 33);
    }

    public LootSpawnerContainer(int id, PlayerInventory playerInventory, PacketBuffer data) {
        this(id, playerInventory, getTile(playerInventory.player.world, data));
    }
}
