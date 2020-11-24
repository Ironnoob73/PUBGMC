package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import net.minecraft.item.ItemStack;

public abstract class ReloadAnimation extends GunMultiStepAnimation {

    public ReloadAnimation(AbstractGunItem item, ItemStack stack) {
        super(item.getReloadTime(stack));
    }
}
