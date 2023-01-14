package toma.pubgmc.common.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import toma.pubgmc.api.capability.BoostStats;
import toma.pubgmc.api.capability.PubgmcCapabilities;

public class BoostStatsProvider implements ICapabilitySerializable<CompoundTag> {

    private final LazyOptional<BoostStats> holder;

    public BoostStatsProvider(Player player) {
        this.holder = LazyOptional.of(() -> new PlayerBoostStats(player));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return PubgmcCapabilities.BOOST_STATS.orEmpty(cap, holder);
    }

    @Override
    public CompoundTag serializeNBT() {
        return holder.map(INBTSerializable::serializeNBT).orElse(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        holder.ifPresent(stats -> stats.deserializeNBT(nbt));
    }
}
