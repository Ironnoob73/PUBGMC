package dev.toma.pubgmc.client.animation.gun;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.types.AimingAnimation;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;

public class AnimationPackS12K extends AnimationPackShotguns {

    @Override
    public float getAimYOffset(GunItem item, ItemStack stack) {
        return 0.125F;
    }

    @Override
    public AimingAnimation getAimingAnimation(GunItem gunItem, ItemStack stack) {
        return new AimingAnimation(-0.41F, this.getAimYOffset(gunItem, stack), 0.25F).left(f -> {
            GlStateManager.translatef(-0.3F * f, 0.1F * f, 0.15F * f);
            GlStateManager.rotatef(2.0F * f, 0.0F, 1.0F, 0.0F);
        });
    }
}
