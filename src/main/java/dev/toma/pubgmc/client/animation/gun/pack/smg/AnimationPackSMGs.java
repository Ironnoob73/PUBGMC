package dev.toma.pubgmc.client.animation.gun.pack.smg;

import dev.toma.pubgmc.client.animation.gun.ReloadAnimation;
import dev.toma.pubgmc.client.animation.gun.pack.GunAnimationPack;
import dev.toma.pubgmc.client.animation.types.AimingAnimation;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import net.minecraft.item.ItemStack;

public abstract class AnimationPackSMGs extends GunAnimationPack {

    @Override
    public void applyOnLeftArm() {
        super.applyOnLeftArm();
    }

    @Override
    public void applyOnRightArm() {
        super.applyOnRightArm();
    }

    @Override
    public float getAimYOffset(AbstractGunItem item, ItemStack stack) {
        return super.getAimYOffset(item, stack);
    }

    @Override
    public AimingAnimation getAimingAnimation(AbstractGunItem gunItem, ItemStack stack) {
        return null;
    }

    @Override
    public ReloadAnimation getReloadAnimation(AbstractGunItem gunItem, ItemStack stack, boolean isContinued) {
        return null;
    }
}
