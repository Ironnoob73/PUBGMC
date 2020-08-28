package dev.toma.pubgmc.inv.cap;

import dev.toma.pubgmc.inv.inventory.MyInventoryWrapper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InventoryFactory extends ItemStackHandler implements PMCInventoryHandler {

    private boolean[] slotStates = new boolean[3];
    private boolean isBlocked;
    private PlayerEntity player;

    public InventoryFactory() {
        super(3);
    }

    public InventoryFactory(PlayerEntity player) {

    }

    public static PMCInventoryHandler getInventoryHandler(PlayerEntity player) {
        PMCInventoryHandler handler = player.getCapability(InventoryProvider.INVENTORY_HANDLER, null).orElseThrow(NullPointerException::new);
        handler.setOwner(player);
        return handler;
    }

    public static MyInventoryWrapper getInventory(PlayerEntity player) {
        PMCInventoryHandler handler = getInventoryHandler(player);
        return new MyInventoryWrapper(handler, player);
    }

    @Override
    public boolean isValidForSlot(int slotId, PlayerEntity player, ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBlocked() {
        return isBlocked;
    }

    @Override
    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    @Override
    public boolean hasChanged(int slotId) {
        if(slotStates == null) {
            slotStates = new boolean[getSlots()];
        }
        return slotStates[slotId];
    }

    @Override
    public void setChanged(int slotId, boolean changed) {
        if(slotStates == null) {
            slotStates = new boolean[getSlots()];
        }
        slotStates[slotId] = changed;
    }

    @Override
    public void setOwner(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void setSize(int size) {
        if(size < 3) size = 3;
        super.setSize(size);
        boolean[] oldStates = slotStates;
        slotStates = new boolean[size];
        for(int i = 0; i < oldStates.length; i++) {
            slotStates[i] = oldStates[i];
        }
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if(stack.isEmpty() || isValidForSlot(slot, player, stack)) {
            super.setStackInSlot(slot, stack.copy());
        }
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(!isValidForSlot(slot, player, stack)) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        setChanged(slot, true);
    }
}
