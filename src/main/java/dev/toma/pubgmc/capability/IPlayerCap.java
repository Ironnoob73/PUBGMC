package dev.toma.pubgmc.capability;

import dev.toma.pubgmc.capability.player.BoostStats;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerCap extends INBTSerializable<CompoundNBT> {

    void onTick();

    BoostStats getBoostStats();

    CompoundNBT saveNetworkData();

    void loadNetworkData(CompoundNBT nbt);

    void syncNetworkData();

    void syncAllData();
}
