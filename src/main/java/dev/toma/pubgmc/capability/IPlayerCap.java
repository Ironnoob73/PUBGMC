package dev.toma.pubgmc.capability;

import dev.toma.pubgmc.capability.player.AimInfo;
import dev.toma.pubgmc.capability.player.BoostStats;
import dev.toma.pubgmc.capability.player.ReloadInfo;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerCap extends INBTSerializable<CompoundNBT> {

    void onTick();

    AimInfo getAimInfo();

    ReloadInfo getReloadInfo();

    BoostStats getBoostStats();

    CompoundNBT saveNetworkData();

    void setProne(boolean prone);

    boolean isProne();

    void loadNetworkData(CompoundNBT nbt);

    void syncNetworkData();

    void syncAllData();
}
