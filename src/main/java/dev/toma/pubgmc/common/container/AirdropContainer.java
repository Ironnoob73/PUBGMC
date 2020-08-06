package dev.toma.pubgmc.common.container;

import dev.toma.pubgmc.common.tileentity.AirdropTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

import static dev.toma.pubgmc.init.PMCContainers.AIRDROP;

public class AirdropContainer extends AbstractModContainer<AirdropTileEntity> {

    public AirdropContainer(int windowID, PlayerInventory playerInventory, AirdropTileEntity tileEntity) {
        super(AIRDROP.get(), windowID, playerInventory, tileEntity);
    }

    public AirdropContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, getTile(inventory.player.world, buffer));
    }
}
