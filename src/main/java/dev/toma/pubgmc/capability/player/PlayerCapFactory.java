package dev.toma.pubgmc.capability.player;

import dev.toma.pubgmc.capability.IPlayerCap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class PlayerCapFactory implements IPlayerCap {

    private PlayerEntity owner;

    public PlayerCapFactory() {
        this(null);
    }

    public PlayerCapFactory(PlayerEntity owner) {
        this.owner = owner;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
