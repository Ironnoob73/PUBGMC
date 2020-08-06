package dev.toma.pubgmc.common.container;

import dev.toma.pubgmc.common.tileentity.AirdropTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import static dev.toma.pubgmc.init.PMCContainers.AIRDROP;

public class AirdropContainer extends AbstractModContainer<AirdropTileEntity> {

    public AirdropContainer(int windowID, PlayerInventory playerInventory, AirdropTileEntity tileEntity) {
        super(AIRDROP.get(), windowID, playerInventory, tileEntity);
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(inv -> {
            for(int i = 0; i < 9; i++) {
                addSlot(new SlotItemHandler(inv, i, 8 + i * 18, 20));
            }
        });
        addPlayerInventory(playerInventory, 51);
    }

    public AirdropContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, getTile(inventory.player.world, buffer));
    }
}
