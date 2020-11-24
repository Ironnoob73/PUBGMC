package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import dev.toma.pubgmc.util.AttachmentHelper;
import net.minecraft.item.ItemStack;

public class AnimationPackP1911 extends AnimationPackPistols {

    @Override
    public ReloadAnimation getReloadAnimation(AbstractGunItem gunItem, ItemStack stack, boolean isContinued) {
        return new PistolReloadAnimation(gunItem, stack);
    }

    @Override
    public float getAimYOffset(AbstractGunItem item, ItemStack stack) {
        if (AttachmentHelper.hasRedDot(item, stack)) {
            return 0.05F;
        }
        return 0.1F;
    }
}
