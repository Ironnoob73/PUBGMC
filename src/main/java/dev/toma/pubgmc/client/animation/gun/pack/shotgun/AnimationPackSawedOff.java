package dev.toma.pubgmc.client.animation.gun.pack.shotgun;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.gun.ReloadAnimation;
import dev.toma.pubgmc.client.animation.gun.SimpleGunAnimation;
import dev.toma.pubgmc.client.animation.gun.pack.pistol.AnimationPackPistols;
import dev.toma.pubgmc.client.animation.types.AimingAnimation;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import net.minecraft.item.ItemStack;

public class AnimationPackSawedOff extends AnimationPackPistols {

    @Override
    public ReloadAnimation getReloadAnimation(AbstractGunItem gunItem, ItemStack stack, boolean isContinued) {
        return new SawedOffReloadAnimation(gunItem, stack);
    }

    @Override
    public AimingAnimation getAimingAnimation(AbstractGunItem item, ItemStack stack) {
        return new AimingAnimation(-0.56F, this.getAimYOffset(item, stack), 0.2F).right(f -> {
            GlStateManager.translatef(-0.4F * f, (-0.1F + getAimYOffset(item, stack)) * f, 0.34F * f);
            GlStateManager.rotatef(6.0F * f, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(16.0F * f, 0.0F, 1.0F, 0.0F);
        }).left(f -> {
            GlStateManager.translatef(-0.1F * f, (-0.035F + getAimYOffset(item, stack)) * f, 0.55F * f);
            GlStateManager.rotatef(25.0F * f, 0.0F, 1.0F, 0.0F);
        });
    }

    @Override
    public float getAimYOffset(AbstractGunItem item, ItemStack stack) {
        return 0.2F;
    }

    @Override
    public void applyOnRightArm() {
        GlStateManager.translatef(0.35F, -0.185F, 0.5F);
        GlStateManager.rotatef(10.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(10.0F, 1.0F, 0.0F, 0.0F);
    }

    @Override
    public void applyOnLeftArm() {
        GlStateManager.translatef(0.5F, -0.2F, -0.2F);
        GlStateManager.rotatef(-25.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(10.0F, 1.0F, 0.0F, 0.0F);
    }

    public static class SawedOffReloadAnimation extends ReloadAnimation {

        public SawedOffReloadAnimation(AbstractGunItem item, ItemStack stack) {
            super(item, stack);
        }

        @Override
        public void initAnimation() {
            addStep(0F, 0.05F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, +0.25F * f);
                GlStateManager.rotatef(+5F * f, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.05F, 0.15F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F);
                GlStateManager.rotatef(5F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.rotatef(+12F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(+6F * f, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.15F, 0.2F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F);
                GlStateManager.rotatef(5F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.rotatef(12F - 8F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(6F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.2F, 0.3F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F);
                GlStateManager.rotatef(5F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.rotatef(4F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(6F - 4F * f, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                GlStateManager.translatef(0.0F, 8.0F * f, -15.0F * f);
                GlStateManager.rotatef(45.0F * f, 1.0F, 0.0F, 0.0F);
            }).create());
            // 0.3 - 0.45
            addStep(0.3F, 0.45F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F);
                GlStateManager.rotatef(5F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.rotatef(4F + 43F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(2F - 30F * f, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                GlStateManager.translatef(0.0F, 8.0F, -15.0F);
                GlStateManager.rotatef(45.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.45F, 0.6F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F);
                GlStateManager.rotatef(5F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, +0.1F * f);
                GlStateManager.rotatef(47F - 46F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-28F + 30F * f, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                GlStateManager.translatef(0.0F, 8.0F, -15.0F);
                GlStateManager.rotatef(45.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.6F, 0.65F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F);
                GlStateManager.rotatef(5F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.1F);
                GlStateManager.rotatef(1F + 1F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(2F - 2F * f, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                GlStateManager.translatef(0.0F, 8.0F, -15.0F);
                GlStateManager.rotatef(45.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.65F, 0.7F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F);
                GlStateManager.rotatef(5F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.1F);
                GlStateManager.rotatef(2F - 5F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(+2F * f, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                GlStateManager.translatef(0.0F, 8.0F, -15.0F);
                GlStateManager.rotatef(45.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.7F, 0.75F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F);
                GlStateManager.rotatef(5F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.1F);
                GlStateManager.rotatef(-3F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(2F - 2F * f, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                GlStateManager.translatef(0.0F, 8.0F, -15.0F);
                GlStateManager.rotatef(45.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.75F, 0.85F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F);
                GlStateManager.rotatef(5F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.1F);
                GlStateManager.rotatef(-3F + 8F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-13F * f, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                GlStateManager.translatef(0.0F, 8.0F, -15.0F);
                GlStateManager.rotatef(45.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.85F, 0.95F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F);
                GlStateManager.rotatef(5F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.1F - 0.1F * f);
                GlStateManager.rotatef(5F - 5F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-13F + 13F * f, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                GlStateManager.translatef(0.0F, 8.0F, -15.0F);
                GlStateManager.rotatef(45.0F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.95F, 1F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F - 0.25F * f);
                GlStateManager.rotatef(5F - 5F * f, 1.0F, 0.0F, 0.0F);
            }).create());
        }
    }
}
