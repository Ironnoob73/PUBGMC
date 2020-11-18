package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.client.animation.types.AimingAnimation;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;

public class AnimationPackS686 extends AnimationPackShotguns {

    @Override
    public float getAimYOffset(GunItem item, ItemStack stack) {
        return 0.20F;
    }
}
