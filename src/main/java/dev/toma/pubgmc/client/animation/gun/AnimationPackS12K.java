package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.client.animation.types.AimingAnimation;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;

public class AnimationPackS12K extends AnimationPackShotguns {

    @Override
    public AimingAnimation getAimingAnimation(GunItem gunItem, ItemStack stack) {
        return null;
    }

    @Override
    public ReloadAnimation getReloadAnimation(GunItem gunItem, ItemStack stack) {
        return null;
    }
}
