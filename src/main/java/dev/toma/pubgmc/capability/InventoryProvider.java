package dev.toma.pubgmc.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InventoryProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(PMCInventoryHandler.class)
    public static final Capability<PMCInventoryHandler> INVENTORY_HANDLER = null;
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
}
