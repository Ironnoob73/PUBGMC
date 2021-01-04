package dev.toma.pubgmc.client.animation.gun.pack.pistol;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.gun.ReloadAnimation;
import dev.toma.pubgmc.client.animation.gun.SimpleGunAnimation;
import dev.toma.pubgmc.client.animation.gun.pack.pistol.AnimationPackPistols;
import dev.toma.pubgmc.client.animation.types.AimingAnimation;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import dev.toma.pubgmc.util.AttachmentHelper;
import net.minecraft.item.ItemStack;

public class AnimationPackScorpion extends AnimationPackPistols {

    @Override
    public ReloadAnimation getReloadAnimation(AbstractGunItem gunItem, ItemStack stack, boolean isContinued) {
        return new ScorpionReload(gunItem, stack);
    }

    @Override
    public float getAimYOffset(AbstractGunItem item, ItemStack stack) {
        if (AttachmentHelper.hasRedDot(item, stack)) {
            return 0.045F;
        }
        return 0.1F;
    }

    @Override
    public AimingAnimation getAimingAnimation(AbstractGunItem item, ItemStack stack) {
        return new AimingAnimation(-0.56F, this.getAimYOffset(item, stack), 0.0F).right(f -> {
            GlStateManager.translatef(-0.4F * f, (0.025F + getAimYOffset(item, stack)) * f, 0.1F * f);
            GlStateManager.rotatef(10.0F * f, 0.0F, 1.0F, 0.0F);
        }).left(f -> {
            GlStateManager.translatef(-0.35F * f, (0.025F + getAimYOffset(item, stack)) * f, 0.2F * f);
            GlStateManager.rotatef(8.0F * f, 0.0F, 1.0F, 0.0F);
        });
    }

    @Override
    public void applyOnRightArm() {
        GlStateManager.translatef(0.35F, -0.20F, 0.45F);
        GlStateManager.rotatef(10.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(10.0F, 1.0F, 0.0F, 0.0F);
    }

    @Override
    public void applyOnLeftArm() {
        GlStateManager.translatef(0.55F, -0.15F, -0.1F);
        GlStateManager.rotatef(-18.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(8.0F, 1.0F, 0.0F, 0.0F);
    }

    public static class ScorpionReload extends ReloadAnimation {

        ScorpionReload(AbstractGunItem item, ItemStack stack) {
            super(item, stack);
        }

        @Override
        public void initAnimation() {
            addStep(0F, 0.15F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, +0.1F * f);
                GlStateManager.rotatef(+10F * f, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.rotatef(-4F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-5F * f, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.15F, 0.3F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.1F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.rotatef(-4F + 20F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-5F - 48F * f, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                GlStateManager.translatef(0, 40.0F * f, 0.0F);
            }).create());
            addStep(0.3F, 0.45F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.1F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.rotatef(16F - 19F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-53F + 45F * f, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                GlStateManager.translatef(0F, 40.0F - 40.0F * f, 0.0F);
            }).create());
            addStep(0.45F, 0.5F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.1F);
                GlStateManager.rotatef(10F + 3F * f, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.rotatef(-3F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-8F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.5F, 0.6F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.1F - 0.1F * f);
                GlStateManager.rotatef(13F - 13F * f, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.rotatef(-3F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-8F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.6F, 0.75F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.rotatef(0.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, +0.1F * f);
                GlStateManager.rotatef(-3F + 7F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-8F + 16F * f, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.75F, 0.85F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.rotatef(0.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.1F + 0.15F * f);
                GlStateManager.rotatef(4F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(8F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.85F, 1F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.rotatef(0.0F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.25F - 0.25F * f);
                GlStateManager.rotatef(4F - 4F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(8F - 8F * f, 1.0F, 0.0F, 0.0F);
            }).create());

        }
    }
}
