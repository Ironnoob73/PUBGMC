package dev.toma.pubgmc.common.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;

public abstract class InventoryTileEntity extends TileEntity implements IInventory {

    protected NonNullList<ItemStack> inventory;

    public InventoryTileEntity(TileEntityType<?> type) {
        super(type);
        this.inventory = this.createInventory();
    }

    public abstract NonNullList<ItemStack> createInventory();

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : this.inventory) {
            if(!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.isValidIndex(index) ? inventory.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if(this.isValidIndex(index)) {
            this.inventory.set(index, stack);
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        for(int i = 0; i < inventory.size(); i++) {
            this.inventory.set(i, ItemStack.EMPTY);
        }
    }

    protected boolean isValidIndex(int index) {
        return index >= 0 && index < inventory.size();
    }
}
