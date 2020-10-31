package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;

public class AnimationPackP1911 extends AnimationPackPistols {

    @Override
    public ReloadAnimation getReloadAnimation(GunItem gunItem, ItemStack stack) {
        return new ReloadAnimationP92(gunItem, stack);
    }
}
