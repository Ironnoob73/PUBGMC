package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;

public class AnimationPackR1895 extends AnimationPackPistols {

    // TODO
    @Override
    public ReloadAnimation getReloadAnimation(GunItem gunItem, ItemStack stack) {
        return new PistolDefaultReloadAnimation(gunItem, stack);
    }

    @Override
    public float getAimYOffset(GunItem item, ItemStack stack) {
        return super.getAimYOffset(item, stack);
    }
}
