package dev.toma.pubgmc.capability.player;

import dev.toma.pubgmc.capability.IPlayerCap;
import net.minecraft.nbt.CompoundNBT;

public class DummyImpl implements IPlayerCap {

    private final BoostStats stats;

    public DummyImpl() {
        stats = new BoostStats(this);
    }

    @Override
    public void onTick() {

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
