package dev.toma.pubgmc.common.container;

import dev.toma.pubgmc.common.tileentity.DeathCrateTileEntity;
import dev.toma.pubgmc.init.PMCContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DeathCrateContainer extends AbstractModContainer<DeathCrateTileEntity> {

    public DeathCrateContainer(int windowID, PlayerInventory playerInventory, DeathCrateTileEntity tileEntity) {
        super(PMCContainers.DEATH_CRATE.get(), windowID, playerInventory, tileEntity);
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(inventory -> {
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 9; x++) {
                    addSlot(new SlotItemHandler(inventory, x + (y * 9), 8 + x * 18, 20 + y * 18));
                }
            }
        });
        addPlayerInventory(playerInventory, 126);
    }

    public DeathCrateContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, getTile(inventory.player.world, buffer));
    }
}
