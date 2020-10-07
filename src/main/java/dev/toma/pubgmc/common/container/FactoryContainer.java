package dev.toma.pubgmc.common.container;

import dev.toma.pubgmc.common.tileentity.AbstractFactoryTileEntity;
import dev.toma.pubgmc.init.PMCContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class FactoryContainer extends AbstractModContainer<AbstractFactoryTileEntity> {

    public FactoryContainer(int id, PlayerInventory inventory, AbstractFactoryTileEntity tileEntity) {
        super(PMCContainers.FACTORY.get(), id, inventory, tileEntity);
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(cap -> {
            for(int y = 0; y < 2; y++) {
                for(int x = 0; x < 4; x++) {
                    addSlot(new SlotItemHandler(cap, x + y * 4, 53 + x * 18, 10 + y * 18));
                }
            }
            addSlot(new SlotItemHandler(cap, 8, 80, 74) {
                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return false;
                }
            });
        });
        addPlayerInventory(inventory, 109);
    }

    public FactoryContainer(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, getTile(inventory.player.world, buffer));
    }
}
