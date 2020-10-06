package dev.toma.pubgmc.capability.player;

import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketSendNBT;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class PlayerCapFactory implements IPlayerCap {

    private final PlayerEntity owner;

    private final BoostStats boostStats;
    private final AimInfo aimInfo;
    private final ReloadInfo reloadInfo;
    private boolean prone;

    public PlayerCapFactory(PlayerEntity owner) {
        this.owner = owner;
        this.boostStats = new BoostStats(this);
        this.aimInfo = new AimInfo(this);
        this.reloadInfo = new ReloadInfo(this);
    }

    @Override
    public void onTick() {
        if(owner == null) return;
        boostStats.tick(owner);
        aimInfo.onUpdate();
        reloadInfo.onUpdate();
    }

    @Override
    public BoostStats getBoostStats() {
        return boostStats;
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
    public void setProne(boolean prone) {
        this.prone = prone;
    }

    @Override
    public boolean isProne() {
        return prone;
    }

    @Override
    public CompoundNBT saveNetworkData() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("boostData", boostStats.save());
        nbt.put("aimInfo", aimInfo.write());
        nbt.put("reloadInfo", reloadInfo.write());
        nbt.putBoolean("prone", prone);
        return nbt;
    }

    @Override
    public void loadNetworkData(CompoundNBT nbt) {
        boostStats.load(nbt.contains("boostData") ? nbt.getCompound("boostData") : new CompoundNBT());
        aimInfo.read(nbt.contains("aimInfo") ? nbt.getCompound("aimInfo") : new CompoundNBT());
        reloadInfo.read(nbt.contains("reloadInfo") ? nbt.getCompound("reloadInfo") : new CompoundNBT());
        prone = nbt.getBoolean("prone");
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("network", saveNetworkData());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        loadNetworkData(nbt.contains("network") ? nbt.getCompound("network") : new CompoundNBT());
    }

    @Override
    public void syncNetworkData() {
        if(owner instanceof ServerPlayerEntity) {
            NetworkManager.sendToClient((ServerPlayerEntity) owner, new CPacketSendNBT(saveNetworkData(), CPacketSendNBT.PLAYER_CAP_SYNC_NETWORK));
        }
    }

    @Override
    public void syncAllData() {
        if(owner instanceof ServerPlayerEntity) {
            NetworkManager.sendToClient((ServerPlayerEntity) owner, new CPacketSendNBT(serializeNBT(), CPacketSendNBT.PLAYER_CAP_SYNC_FULL));
        }
    }

    public PlayerEntity getOwner() {
        return owner;
    }

    public static IPlayerCap get(PlayerEntity player) {
        return player.getCapability(PlayerCapProvider.CAP, null).orElse(PlayerCapProvider.dummyCap);
    }
}
