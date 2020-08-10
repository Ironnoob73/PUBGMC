package dev.toma.pubgmc.client.animation.gun;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.item.ItemStack;

public class ReloadAnimationP92 extends ReloadAnimation {

    public ReloadAnimationP92(GunItem item, ItemStack stack) {
        super(item, stack);
    }

    @Override
    public void initAnimation() {
        addStep(0F, 0.1F, SimpleGunAnimation.create().itemHand(f -> {
            GlStateManager.translatef(0.0F, -0.2F * f, 0.0F);
            GlStateManager.rotatef(+20F * f, 0.0F, 0.0F, 1.0F);
        }).leftHand(f -> {
            GlStateManager.translatef(0.0F, -0.2F * f, 0.0F);
            GlStateManager.rotatef(+10F * f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-10F * f, 1.0F, 0.0F, 0.0F);
        }).create());
        addStep(0.1F, 0.3F, SimpleGunAnimation.create().itemHand(f -> {
            GlStateManager.translatef(0.0F, -0.2F, 0.0F);
            GlStateManager.rotatef(-9F * f, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(20F, 0.0F, 0.0F, 1.0F);
        }).leftHand(f -> {
            GlStateManager.translatef(0.0F, -0.2F, 0.0F);
            GlStateManager.rotatef(10F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
        }).create());
        addStep(0.3F, 0.45F, SimpleGunAnimation.create().itemHand(f -> {
            GlStateManager.translatef(0.0F, -0.2F, 0.0F);
            GlStateManager.rotatef(-9F + 9F * f, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(20F, 0.0F, 0.0F, 1.0F);
        }).leftHand(f -> {
            GlStateManager.translatef(0.0F, -0.2F, 0.0F);
            GlStateManager.rotatef(10F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
        }).create());
        addStep(0.45F, 0.6F, SimpleGunAnimation.create().itemHand(f -> {
            GlStateManager.translatef(0.0F, -0.2F, 0.0F);
            GlStateManager.rotatef(+20F * f, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(20F - 20F * f, 0.0F, 0.0F, 1.0F);
        }).leftHand(f -> {
            GlStateManager.translatef(0.0F, -0.2F + 0.2F * f, -0.1F * f);
            GlStateManager.rotatef(10F - 10F * f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
        }).create());
        addStep(0.6F, 0.75F, SimpleGunAnimation.create().itemHand(f -> {
            GlStateManager.translatef(0.0F, -0.2F, +0.1F * f);
            GlStateManager.rotatef(20F - 10F * f, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(0.0F, 0.0F, 0.0F, 1.0F);
        }).leftHand(f -> {
            GlStateManager.translatef(-0.2F * f, 0F + 0.3F * f, -0.1F + 0F * f);
            GlStateManager.rotatef(-10F + 10F * f, 1.0F, 0.0F, 0.0F);
        }).create());
        addStep(0.75F, 0.85F, SimpleGunAnimation.create().itemHand(f -> {
            GlStateManager.translatef(0.0F, -0.2F - 0.1F * f, 0.1F);
            GlStateManager.rotatef(10F + 10F * f, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(0.0F, 0.0F, 0.0F, 1.0F);
        }).leftHand(f -> {
            GlStateManager.translatef(-0.2F, 0.3F, -0.1F);
            GlStateManager.rotatef(-8F * f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-5F * f, 1.0F, 0.0F, 0.0F);
        }).create());
        addStep(0.85F, 1F, SimpleGunAnimation.create().itemHand(f -> {
            GlStateManager.translatef(0.0F, -0.3F + 0.3F * f, 0.1F - 0.1F * f);
            GlStateManager.rotatef(20F - 20F * f, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(0.0F, 0.0F, 0.0F, 1.0F);
        }).leftHand(f -> {
            GlStateManager.translatef(-0.2F + 0.2F * f, 0.3F - 0.3F * f, -0.1F + 0.1F * f);
            GlStateManager.rotatef(-8F + 8F * f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(-5F + 5F * f, 1.0F, 0.0F, 0.0F);
        }).create());

    }
}
