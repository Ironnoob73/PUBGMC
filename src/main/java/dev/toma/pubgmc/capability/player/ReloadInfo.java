package dev.toma.pubgmc.capability.player;

import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ReloadInfo {

    private final PlayerCapFactory factory;
    private int slotIn;
    private int total;
    private int left;
    private boolean reloading;

    public ReloadInfo(PlayerCapFactory factory) {
        this.factory = factory;
    }

    public void onUpdate() {
        if(reloading) {
            int slot = factory.getOwner().inventory.currentItem;
            boolean onServer = !factory.getOwner().world.isRemote;
            if(onServer && slotIn != slot) {
                cancelReload();
                factory.syncNetworkData();
                return;
            }
            if(--left <= 0 && onServer) {
                cancelReload();
                factory.syncNetworkData();
                PlayerEntity player = factory.getOwner();
                ItemStack stack = player.getHeldItemMainhand();
                if(stack.getItem() instanceof GunItem) {
                    ((GunItem) stack.getItem()).doReload(player, player.world, stack);
                }
            }
        }
    }

    public void startReloading(int slot, int time) {
        if(!reloading) {
            PlayerEntity player = factory.getOwner();
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof GunItem) {
                // TODO play reload sound
                // TODO reload animation
            }
            reloading = true;
            slotIn = slot;
            total = time;
            left = time;
        }
    }

    public void cancelReload() {
        reloading = false;
        total = 0;
        left = 0;
        slotIn = -1;
    }

    public int getTotal() {
        return total;
    }

    public int getLeft() {
        return left;
    }

    public boolean isReloading() {
        return reloading;
    }

    public float getReloadProgress() {
        return left / (float) total;
    }

    public CompoundNBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("reloading", reloading);
        nbt.putInt("slot", slotIn);
        nbt.putInt("total", total);
        nbt.putInt("left", left);
        return nbt;
    }

    public void read(CompoundNBT nbt) {
        reloading = nbt.getBoolean("reloading");
        slotIn = nbt.getInt("slot");
        total = nbt.getInt("total");
        left = nbt.getInt("left");
    }
}
