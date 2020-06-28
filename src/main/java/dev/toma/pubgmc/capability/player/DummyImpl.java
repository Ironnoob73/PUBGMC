package dev.toma.pubgmc.capability.player;

import dev.toma.pubgmc.capability.IPlayerCap;
import net.minecraft.nbt.CompoundNBT;

public class DummyImpl implements IPlayerCap {

    private final BoostStats stats;
    private final AimInfo aimInfo;
    private final ReloadInfo reloadInfo;

    public DummyImpl() {
        stats = new BoostStats(this);
        aimInfo = new AimInfo(null);
        reloadInfo = new ReloadInfo(null);
    }

    @Override
    public void onTick() {

    }

    @Override
    public AimInfo getAimInfo() {
        return aimInfo;
    }

    @Override
    public ReloadInfo getReloadInfo() {
        return reloadInfo;
    }

    @Override
    public CompoundNBT saveNetworkData() {
        return new CompoundNBT();
    }

    @Override
    public void loadNetworkData(CompoundNBT nbt) {

    }

    @Override
    public CompoundNBT serializeNBT() {
        return saveNetworkData();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    @Override
    public BoostStats getBoostStats() {
        return stats;
    }

    @Override
    public void syncNetworkData() {

    }

    @Override
    public void syncAllData() {

    }
}
