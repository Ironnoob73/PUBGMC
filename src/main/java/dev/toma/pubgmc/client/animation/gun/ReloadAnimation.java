package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;

public abstract class ReloadAnimation extends GunMultiStepAnimation {

    public ReloadAnimation(GunItem item, ItemStack stack) {
        super(item.getReloadTime(stack));
    }
}
