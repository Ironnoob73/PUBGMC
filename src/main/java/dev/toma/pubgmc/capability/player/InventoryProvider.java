package dev.toma.pubgmc.capability.player;

import dev.toma.pubgmc.capability.PMCInventoryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InventoryProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(PMCInventoryHandler.class)
    public static final Capability<PMCInventoryHandler> INVENTORY_HANDLER = null;
    public static final PMCInventoryHandler DUMMY = new DummyHandler();
    final InventoryFactory instance;

    public InventoryProvider(InventoryFactory factory) {
        this.instance = factory;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == INVENTORY_HANDLER ? LazyOptional.of(() -> (T) this.instance) : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        instance.deserializeNBT(nbt);
    }

    // just dummy class
    public static class Storage implements Capability.IStorage<PMCInventoryHandler> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<PMCInventoryHandler> capability, PMCInventoryHandler instance, Direction side) {
            return null;
        }

        @Override
        public void readNBT(Capability<PMCInventoryHandler> capability, PMCInventoryHandler instance, Direction side, INBT nbt) {

        }
    }

    public static class DummyHandler extends ItemStackHandler implements PMCInventoryHandler {

        public DummyHandler() {

        }

        @Override
        public boolean isValidForSlot(int slotId, PlayerEntity player, ItemStack stack) {
            return false;
        }

        @Override
        public boolean isBlocked() {
            return true;
        }

        @Override
        public void setBlocked(boolean blocked) {

        }

        @Override
        public boolean hasChanged(int slotId) {
            return false;
        }

        @Override
        public void setChanged(int slotId, boolean changed) {

        }

        @Override
        public PlayerEntity getOwner() {
            return null;
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {

        }

        @Override
        public int getSlots() {
            return 3;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return ItemStack.EMPTY;
        }
    }
}
