package dev.toma.pubgmc.capability.player;

import dev.toma.pubgmc.capability.IPlayerCap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class BoostStats {

    private final IPlayerCap instanceHolder;
    private int value;
    private float saturation = 0.0F;

    public BoostStats(IPlayerCap playerCap) {
        this.instanceHolder = playerCap;
    }

    public void tick(PlayerEntity player) {
        if (!player.world.isRemote && value > 10) {
            player.addPotionEffect(new EffectInstance(Effects.SPEED, 20, 0, false, false, false));
        }
        if ((saturation = Math.max(0.0F, saturation - 0.002F)) <= 0.0F && value > 0) {
            if (!player.world.isRemote) {
                player.heal(value > 10 ? 2.0F : 1.0F);
                --value;
                saturation = 1.0F;
                instanceHolder.syncNetworkData();
            }
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public CompoundNBT save() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("value", value);
        nbt.putFloat("saturation", saturation);
        return nbt;
    }

    public void load(CompoundNBT nbt) {
        value = nbt.getInt("value");
        saturation = nbt.getFloat("saturation");
    }
}
