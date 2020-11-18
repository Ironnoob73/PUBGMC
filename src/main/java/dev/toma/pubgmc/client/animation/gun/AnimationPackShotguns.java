package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.client.animation.types.AimingAnimation;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;

public abstract class AnimationPackShotguns extends GunAnimationPack {

    @Override
    public void applyOnRightArm() {

    }

    @Override
    public void applyOnLeftArm() {

    }

    @Override
    public AimingAnimation getAimingAnimation(GunItem gunItem, ItemStack stack) {
        return null;
    }
}
