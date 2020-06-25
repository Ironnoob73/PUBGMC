package dev.toma.pubgmc.common.item.gun;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ReloadManager {

    void doReload(PlayerEntity player, World world, ItemStack stack);

    class Magazine implements ReloadManager {

        public static final Magazine instance = new Magazine();

        @Override
        public void doReload(PlayerEntity player, World world, ItemStack stack) {

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

        }

        @Override
        public String toString() {
            return "Single reload manager";
        }
    }

    class Special implements ReloadManager {

        public static final Special instance = new Special();

        @Override
        public void doReload(PlayerEntity player, World world, ItemStack stack) {

        }

        @Override
        public String toString() {
            return "Special reload manager";
        }
    }
}
