package dev.toma.pubgmc.common.container;

import dev.toma.pubgmc.common.tileentity.FlareAirdropTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import static dev.toma.pubgmc.init.PMCContainers.FLARE_AIRDROP;

public class FlareAirdropContainer extends AbstractModContainer<FlareAirdropTileEntity> {

    public FlareAirdropContainer(int id, PlayerInventory inventory, FlareAirdropTileEntity tileEntity) {
        super(FLARE_AIRDROP.get(), id, inventory, tileEntity);
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(inv -> {
            for(int y = 0; y < 2; y++) {
                for(int x = 0; x < 9; x++) {
                    addSlot(new SlotItemHandler(inv, x + y * 9, 8 + x * 18, 9 + y * 18));
                }
            }
        });
        addPlayerInventory(inventory, 51);
    }

    public FlareAirdropContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, getTile(inventory.player.world, buffer));
    }
}
