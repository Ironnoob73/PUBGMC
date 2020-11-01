package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.util.AttachmentHelper;
import net.minecraft.item.ItemStack;

public class AnimationPackR45 extends AnimationPackPistols {

    @Override
    public ReloadAnimation getReloadAnimation(GunItem gunItem, ItemStack stack) {
        return new PistolDefaultReloadAnimation(gunItem, stack);
    }

    @Override
    public float getAimYOffset(GunItem item, ItemStack stack) {
        if(AttachmentHelper.hasRedDot(item, stack)) {
            return 0.05F;
        }
        return 0.1F;
    }
}
