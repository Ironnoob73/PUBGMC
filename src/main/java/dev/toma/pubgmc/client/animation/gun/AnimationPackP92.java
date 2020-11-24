package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import net.minecraft.item.ItemStack;

public class AnimationPackP92 extends AnimationPackPistols {

    @Override
    public ReloadAnimation getReloadAnimation(AbstractGunItem gunItem, ItemStack stack, boolean isContinued) {
        return new PistolReloadAnimation(gunItem, stack);
    }
}
