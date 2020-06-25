package dev.toma.pubgmc.capability.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class AimInfo {

    private final PlayerCapFactory factory;
    private int slotIn;
    private boolean aiming;
    private float progress;
    private float speed = 0.25F;

    public AimInfo(PlayerCapFactory factory) {
        this.factory = factory;
    }

    public void onUpdate() {
        PlayerEntity player = factory.getOwner();
        boolean server = !player.world.isRemote;
        int equippedSlot = player.inventory.currentItem;
        // TODO reload check
        if(server && (this.slotIn != equippedSlot || player.isSprinting())) {
            factory.syncNetworkData();
            setAiming(false, 0.3F);
        }
        if(aiming && progress < 1.0F) {
            progress = Math.min(1.0F, progress + speed);
        } else if(!aiming && progress > 0.0F) {
            progress = Math.max(0.0F, progress - speed);
        }
    }

    public boolean isAiming() {
        return progress == 1.0F;
    }

    public float getProgress() {
        return progress;
    }

    public CompoundNBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("slot", slotIn);
        nbt.putBoolean("aim", aiming);
        nbt.putFloat("progress", progress);
        nbt.putFloat("speed", speed);
        return nbt;
    }

    public void read(CompoundNBT nbt) {
        slotIn = nbt.getInt("slot");
        aiming = nbt.getBoolean("aim");
        progress = nbt.getFloat("progress");
        speed = nbt.getFloat("speed");
    }

    public void setAiming(boolean aiming, float speed) {
        if(aiming) {
            this.slotIn = factory.getOwner().inventory.currentItem;
        }
        this.aiming = aiming;
        this.speed = speed;
    }


}
