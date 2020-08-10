package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.client.animation.types.AimingAnimation;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class GunAnimationPack {

    public void applyOnLeftArm() {

    }

    public void applyOnRightArm() {

    }

    public abstract AimingAnimation getAimingAnimation(GunItem gunItem, ItemStack stack);

    public abstract ReloadAnimation getReloadAnimation(GunItem gunItem, ItemStack stack);
}
