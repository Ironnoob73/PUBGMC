package dev.toma.pubgmc.common.container;

import dev.toma.pubgmc.capability.PMCInventoryHandler;
import dev.toma.pubgmc.common.item.utility.BackpackSlotItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class BackpackLockableSlot extends Slot {

    final PMCInventoryHandler handler;

    public BackpackLockableSlot(IInventory inventory, int slotIndex, int x, int y, PMCInventoryHandler handler) {
        super(inventory, slotIndex, x, y);
        this.handler = handler;
    }

    @Override
    public final boolean isItemValid(ItemStack stack) {
        return valid();
    }

    @Override
    public final boolean canTakeStack(PlayerEntity playerIn) {
        return playerIn.isCreative() || valid();
    }

    @Nullable
    @Override
    public final String getSlotTexture() {
        return valid() ? null : "pubgmc:slot/locked";
    }

    final boolean valid() {
        ItemStack stack = handler.getStackInSlot(2);
        if(!stack.isEmpty() && stack.getItem() instanceof BackpackSlotItem) {
            int row = 2 - (this.getSlotIndex() - 9) / 9;
            int access = ((BackpackSlotItem) stack.getItem()).getType().ordinal();
            return row <= access;
        }
        return false;
    }
}
