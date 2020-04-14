package dev.toma.pubgmc.capability.player;

import dev.toma.pubgmc.capability.IPlayerCap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerCapProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IPlayerCap.class)
    public static Capability<IPlayerCap> CAP = null;
    public LazyOptional<IPlayerCap> instance = LazyOptional.of(CAP::getDefaultInstance);
    protected static DummyImpl dummyCap = new DummyImpl();

    public PlayerCapProvider() {
    }

    public PlayerCapProvider(PlayerEntity playerEntity) {
        this.instance = LazyOptional.of(() -> new PlayerCapFactory(playerEntity));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) CAP.getStorage().writeNBT(CAP, instance.orElse(dummyCap), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CAP.getStorage().readNBT(CAP, instance.orElse(dummyCap), null, nbt);
    }
}
