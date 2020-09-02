package dev.toma.pubgmc.capability;

import dev.toma.pubgmc.common.inventory.MyInventoryWrapper;
import dev.toma.pubgmc.common.inventory.SlotType;
import dev.toma.pubgmc.common.item.utility.BackpackSlotItem;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketSyncInventory;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InventoryFactory extends ItemStackHandler implements PMCInventoryHandler {

    private boolean[] slotStates = new boolean[3];
    private boolean isBlocked;
    private final PlayerEntity player;

    public InventoryFactory() {
        this(null);
    }

    public InventoryFactory(PlayerEntity player) {
        super(3);
        this.player = player;
    }

    public static ItemStack getStackInSlot(PlayerEntity player, SlotType type) {
        return getInventoryHandler(player).getStackInSlot(type.ordinal());
    }

    public static PMCInventoryHandler getInventoryHandler(PlayerEntity player) {
        return player.getCapability(InventoryProvider.INVENTORY_HANDLER, null).orElse(InventoryProvider.DUMMY);
    }

    public static MyInventoryWrapper getInventory(PlayerEntity player) {
        PMCInventoryHandler handler = getInventoryHandler(player);
        return new MyInventoryWrapper(handler, player);
    }

    @Override
    public PlayerEntity getOwner() {
        return player;
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
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack itemStack = super.extractItem(slot, amount, simulate);
        if (slot == 2 && !simulate) {
            ItemStack stack = getStackInSlot(2);
            World world = player.world;
            PlayerInventory playerInventory = player.inventory;
            if(stack.isEmpty()) {
                dropInventoryContents(world, playerInventory, 27);
            }
        }
        return itemStack;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(!isValidForSlot(slot, player, stack)) return stack;
        if (slot == 2) {
            World world = player.world;
            PlayerInventory playerInventory = player.inventory;
            if(stack.isEmpty()) {
                dropInventoryContents(world, playerInventory, 0);
            } else if(stack.getItem() instanceof BackpackSlotItem) {
                int level = 2 - ((BackpackSlotItem) stack.getItem()).getType().ordinal();
                if(level > 0) {
                    int x = level * 9;
                    dropInventoryContents(world, playerInventory, x);
                }
            }
        }
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        setChanged(slot, true);
    }

    private void dropInventoryContents(World world, PlayerInventory inventory, int from) {
        for(int i = 0; i < from; i++) {
            int idx = i + 9;
            ItemStack stack = inventory.getStackInSlot(idx);
            if(!stack.isEmpty()) {
                if(!world.isRemote) {
                    ItemEntity itemEntity = new ItemEntity(world, player.posX, player.posY, player.posZ, stack.copy());
                    itemEntity.setPickupDelay(40);
                    world.addEntity(itemEntity);
                }
                inventory.removeStackFromSlot(idx);
            }
        }
    }
}
