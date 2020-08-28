package dev.toma.pubgmc.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface PMCInventoryHandler extends IItemHandlerModifiable {

    boolean isValidForSlot(int slotId, PlayerEntity player, ItemStack stack);

    boolean isBlocked();

    void setBlocked(boolean blocked);

    boolean hasChanged(int slotId);

    void setChanged(int slotId, boolean changed);

    PlayerEntity getOwner();
}
