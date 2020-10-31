package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;

public class AnimationPackP92 extends AnimationPackPistols {

    @Override
    public ReloadAnimation getReloadAnimation(GunItem gunItem, ItemStack stack) {
        return new PistolDefaultReloadAnimation(gunItem, stack);
    }
}
