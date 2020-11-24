package dev.toma.pubgmc.common.item.gun;

import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.capability.player.ReloadInfo;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import dev.toma.pubgmc.common.item.utility.AmmoItem;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketAnimation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ReloadManager {

    void doReload(PlayerEntity player, World world, ItemStack stack);

    boolean canInterrupt(AbstractGunItem gun, ItemStack stack);

    class Magazine implements ReloadManager {

        public static final Magazine instance = new Magazine();

        @Override
        public void doReload(PlayerEntity player, World world, ItemStack stack) {
            AbstractGunItem gun = (AbstractGunItem) stack.getItem();
            AmmoType ammoType = gun.getAmmoType();
            int max = gun.getMaxAmmo(stack);
            int actual = gun.getAmmo(stack);
            int left = max - actual;
            if(actual < max) {
                for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    if(player.isCreative()) {
                        left = 0;
                        break;
                    }
                    ItemStack stacc = player.inventory.getStackInSlot(i);
                    if(stacc.getItem() instanceof AmmoItem) {
                        AmmoItem ammoItem = (AmmoItem) stacc.getItem();
                        if(ammoItem.ammoType() == ammoType) {
                            int count = Math.min(left, stacc.getCount());
                            left -= count;
                            stacc.shrink(count);
                            if(left <= 0) break;
                        }
                    }
                }
                gun.setAmmo(stack, max - left);
            }
        }

        @Override
        public boolean canInterrupt(AbstractGunItem gun, ItemStack stack) {
            return false;
        }

        @Override
        public String toString() {
            return "Magazine reload manager";
        }
    }

    class Single implements ReloadManager {

        public static final Single instance = new Single();

        @Override
        public void doReload(PlayerEntity player, World world, ItemStack stack) {
            AbstractGunItem gun = (AbstractGunItem) stack.getItem();
            AmmoType ammoType = gun.getAmmoType();
            int max = gun.getMaxAmmo(stack);
            int ammo = gun.getAmmo(stack);
            boolean continueReloading = max - ammo > 1;
            if(ammo < max) {
                if(player.isCreative()) {
                    gun.setAmmo(stack, ammo + 1);
                    if(continueReloading) {
                        startReload(player);
                    }
                    return;
                }
                for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    ItemStack stacc = player.inventory.getStackInSlot(i);
                    if(stacc.getItem() instanceof AmmoItem) {
                        AmmoItem ammoItem = (AmmoItem) stacc.getItem();
                        if(ammoItem.ammoType() == ammoType) {
                            stacc.shrink(1);
                            gun.setAmmo(stack, ammo + 1);
                            if(continueReloading) {
                                startReload(player);
                            }
                            break;
                        }
                    }
                }
            }
        }

        private void startReload(PlayerEntity player) {
            IPlayerCap cap = PlayerCapFactory.get(player);
            ReloadInfo reloadInfo = cap.getReloadInfo();
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof AbstractGunItem) {
                int time = ((AbstractGunItem) stack.getItem()).getReloadTime(stack);
                reloadInfo.startReloading(player.inventory.currentItem, time);
                cap.syncNetworkData();
                NetworkManager.sendToClient((ServerPlayerEntity) player, new CPacketAnimation(Animations.RELOADING, CPacketAnimation.Result.PLAY));
            }
        }

        @Override
        public String toString() {
            return "Single reload manager";
        }

        @Override
        public boolean canInterrupt(AbstractGunItem gun, ItemStack stack) {
            return true;
        }
    }

    class Special implements ReloadManager {

        public static final Special instance = new Special();

        @Override
        public void doReload(PlayerEntity player, World world, ItemStack stack) {
            AbstractGunItem gunItem = (AbstractGunItem) stack.getItem();
            int ammo = gunItem.getAmmo(stack);
            if(ammo == 0) {
                Magazine.instance.doReload(player, world, stack);
            } else Single.instance.doReload(player, world, stack);
        }

        @Override
        public String toString() {
            return "Special reload manager";
        }

        @Override
        public boolean canInterrupt(AbstractGunItem gun, ItemStack stack) {
            return gun.getAmmo(stack) > 0;
        }

        private void startReload(PlayerEntity player) {
            IPlayerCap cap = PlayerCapFactory.get(player);
            ReloadInfo reloadInfo = cap.getReloadInfo();
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof AbstractGunItem) {
                int time = ((AbstractGunItem) stack.getItem()).getReloadTime(stack);
                reloadInfo.startReloading(player.inventory.currentItem, time);
                cap.syncNetworkData();
            }
        }
    }
}
