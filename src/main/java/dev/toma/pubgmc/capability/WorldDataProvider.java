package dev.toma.pubgmc.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WorldDataProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IWorldCap.class)
    public static final Capability<IWorldCap> CAP = null;
    private final LazyOptional<IWorldCap> instance;

    public WorldDataProvider() {
        this.instance = LazyOptional.of(WorldDataFactory::new);
    }

    public WorldDataProvider(World world) {
        this.instance = LazyOptional.of(() -> new WorldDataFactory(world));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return instance.orElseThrow(NullPointerException::new).serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        instance.orElseThrow(NullPointerException::new).deserializeNBT(nbt);
    }
}
