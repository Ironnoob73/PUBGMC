package dev.toma.pubgmc.client.animation.gun;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import net.minecraft.item.ItemStack;

public class AnimationPackR1895 extends AnimationPackPistols {

    @Override
    public ReloadAnimation getReloadAnimation(AbstractGunItem gunItem, ItemStack stack, boolean isContinued) {
        return new R1895ReloadAnimation(gunItem, stack);
    }

    @Override
    public float getAimYOffset(AbstractGunItem item, ItemStack stack) {
        return 0.1F;
    }

    public static class R1895ReloadAnimation extends ReloadAnimation {

        public R1895ReloadAnimation(AbstractGunItem item, ItemStack stack) {
            super(item, stack);
        }

        @Override
        public void initAnimation() {
            addStep(0F, 0.4F, SimpleGunAnimation.create().leftHand(f -> {
                GlStateManager.rotatef(+10F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(+9F * f, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.4F, 0.6F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, -0.05F * f);
                GlStateManager.rotatef(-3F * f, 1.0F, 0.0F, 0.0F);
            }).rightHand(f -> {
                GlStateManager.rotatef(+3F * f, 0.0F, 0.0F, 1.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, +0.05F * f, -0.05F * f);
                GlStateManager.rotatef(10F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(9F - 2F * f, 1.0F, 0.0F, 0.0F);
            }).item(f -> {
                GlStateManager.rotatef(+3F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.6F, 1F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, -0.05F + 0.05F * f);
                GlStateManager.rotatef(-3F + 3F * f, 1.0F, 0.0F, 0.0F);
            }).rightHand(f -> {
                GlStateManager.rotatef(3F - 3F * f, 0.0F, 0.0F, 1.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, 0.05F - 0.05F * f, -0.05F + 0.05F * f);
                GlStateManager.rotatef(10F - 10F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(7F - 7F * f, 1.0F, 0.0F, 0.0F);
            }).item(f -> {
                GlStateManager.rotatef(3F - 3F * f, 0.0F, 0.0F, 1.0F);
            }).create());

        }
    }
}
