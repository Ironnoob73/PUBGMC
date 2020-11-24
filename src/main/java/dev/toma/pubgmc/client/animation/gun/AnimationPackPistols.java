package dev.toma.pubgmc.client.animation.gun;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.types.AimingAnimation;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import dev.toma.pubgmc.common.item.gun.core.GunItem;
import dev.toma.pubgmc.util.AttachmentHelper;
import net.minecraft.item.ItemStack;

public abstract class AnimationPackPistols extends GunAnimationPack {

    @Override
    public void applyOnLeftArm() {
        GlStateManager.translatef(0.45F, -0.2F, 0.1F);
        GlStateManager.rotatef(-30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(10.0F, 1.0F, 0.0F, 0.0F);
    }

    @Override
    public void applyOnRightArm() {
        GlStateManager.translatef(0.35F, -0.15F, 0.3F);
        GlStateManager.rotatef(10.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(10.0F, 1.0F, 0.0F, 0.0F);
    }

    @Override
    public AimingAnimation getAimingAnimation(AbstractGunItem item, ItemStack stack) {
        return new AimingAnimation(-0.56F, this.getAimYOffset(item, stack), 0.0F).right(f -> {
            GlStateManager.translatef(-0.4F * f, (0.025F + getAimYOffset(item, stack)) * f, 0.1F * f);
            GlStateManager.rotatef(10.0F * f, 0.0F, 1.0F, 0.0F);
        }).left(f -> {
            GlStateManager.translatef(-0.35F * f, (0.025F + getAimYOffset(item, stack)) * f, 0.2F * f);
            GlStateManager.rotatef(15.0F * f, 0.0F, 1.0F, 0.0F);
        });
    }

    @Override
    public float getAimYOffset(AbstractGunItem item, ItemStack stack) {
        if(AttachmentHelper.hasRedDot(item, stack)) {
            return 0.067F;
        }
        return 0.1F;
    }

    public static class PistolReloadAnimation extends ReloadAnimation {

        public PistolReloadAnimation(AbstractGunItem item, ItemStack stack) {
            super(item, stack);
        }

        @Override
        public void initAnimation() {
            addStep(0F, 0.1F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, -0.1F * f, 0.0F);
                GlStateManager.rotatef(+20F * f, 0.0F, 0.0F, 1.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(-0.6F * f, +0.3F * f, 0.0F);
                GlStateManager.rotatef(-30F * f, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.1F, 0.2F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, -0.1F - 0.1F * f, 0.0F);
                GlStateManager.rotatef(20F + 10F * f, 0.0F, 0.0F, 1.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(-0.6F, 0.3F, 0.0F);
                GlStateManager.rotatef(-30F, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                switch (i) {
                    case 0:
                        GlStateManager.translatef(+3F * f, +27.9F * f, 0.0F);
                        break;
                    case 1:
                        GlStateManager.translatef(0.0F, -0F * f, 0.0F);
                        break;
                }
            }).create());
            addStep(0.2F, 0.35F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, -0.2F + 0.4F * f, 0.0F);
                GlStateManager.rotatef(30F - 40F * f, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(+0.05F * f, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(-0.6F + 0.6F * f, 0.3F - 1F * f, 0.0F);
                GlStateManager.rotatef(-30F + 60F * f, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                switch (i) {
                    case 0:
                        GlStateManager.translatef(3F - 3F * f, 27.9F - 15.7F * f, 0.0F);
                        break;
                    case 1:
                        GlStateManager.translatef(0.0F, -0F, 0.0F);
                        break;
                }
            }).create());
            addStep(0.35F, 0.4F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(0.0F, 0.2F, 0.0F);
                GlStateManager.rotatef(-10F, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(0.05F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, -0.7F + 0.25F * f, +0.05F * f);
                GlStateManager.rotatef(30F, 1.0F, 0.0F, 0.0F);
            }).model((i, f) -> {
                switch (i) {
                    case 0:
                        GlStateManager.translatef(0.0F, 12.2F - 12.2F * f, 0.0F);
                        break;
                    case 1:
                        GlStateManager.translatef(0.0F, -0F, 0.0F);
                        break;
                }
            }).create());
            addStep(0.4F, 0.6F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(-0.4F * f, 0.2F - 0.8F * f, 0.0F);
                GlStateManager.rotatef(-10F + 60F * f, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(0.05F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(-0.3F * f, -0.45F + 0.5F * f, 0.05F - 0.05F * f);
                GlStateManager.rotatef(-30F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(30F - 10F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-20F * f, 0.0F, 0.0F, 1.0F);
            }).model((i, f) -> {
                switch (i) {
                    case 0:
                        GlStateManager.translatef(0.0F, 0F, 0.0F);
                        break;
                    case 1:
                        GlStateManager.translatef(0.0F, -0F, 0.0F);
                        break;
                }
            }).create());
            addStep(0.6F, 0.7F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(-0.4F, -0.6F, 0.0F);
                GlStateManager.rotatef(50F, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(0.05F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(-0.3F, 0.05F, 0F + 0.2F * f);
                GlStateManager.rotatef(-30F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(20F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-20F, 0.0F, 0.0F, 1.0F);
            }).model((i, f) -> {
                switch (i) {
                    case 0:
                        GlStateManager.translatef(0.0F, 0F, 0.0F);
                        break;
                    case 1:
                        GlStateManager.translatef(0.0F, -0F, +9F * f);
                        break;
                }
            }).create());
            addStep(0.7F, 0.8F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(-0.4F, -0.6F + 0.1F * f, 0.0F);
                GlStateManager.rotatef(-10F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(50F, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(0.05F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(-0.3F, 0.05F, 0.2F + 0.1F * f);
                GlStateManager.rotatef(-30F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(20F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-20F, 0.0F, 0.0F, 1.0F);
            }).model((i, f) -> {
                switch (i) {
                    case 0:
                        GlStateManager.translatef(0.0F, 0F, 0.0F);
                        break;
                    case 1:
                        GlStateManager.translatef(0.0F, -0F, 9F - 9F * f);
                        break;
                }
            }).create());
            addStep(0.8F, 0.9F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(-0.4F + 0.3F * f, -0.5F + 0.6F * f, +0.1F * f);
                GlStateManager.rotatef(-10F - 10F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(50F - 40F * f, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(0.05F - 0.05F * f, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(-0.3F + 0.3F * f, 0.05F - 0.05F * f, 0.3F - 0.3F * f);
                GlStateManager.rotatef(-30F + 30F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(20F - 20F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-20F + 20F * f, 0.0F, 0.0F, 1.0F);
            }).model((i, f) -> {
                switch (i) {
                    case 0:
                        GlStateManager.translatef(0.0F, 0F, 0.0F);
                        break;
                    case 1:
                        GlStateManager.translatef(0.0F, -0F, 0.0F);
                        break;
                }
            }).create());
            addStep(0.9F, 1F, SimpleGunAnimation.create().itemHand(f -> {
                GlStateManager.translatef(-0.1F + 0.1F * f, 0.1F - 0.1F * f, 0.1F - 0.1F * f);
                GlStateManager.rotatef(-20F + 20F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(10F - 10F * f, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(-0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.0F, -0F, 0.0F);
                GlStateManager.rotatef(0.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(0.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(0.0F, 0.0F, 0.0F, 1.0F);
            }).model((i, f) -> {
                switch (i) {
                    case 0:
                        GlStateManager.translatef(0.0F, 0F, 0.0F);
                        break;
                    case 1:
                        GlStateManager.translatef(0.0F, -0F, 0.0F);
                        break;
                }
            }).create());
        }
    }

    public static class PistolDefaultReloadAnimation extends ReloadAnimation {

        public PistolDefaultReloadAnimation(AbstractGunItem item, ItemStack stack) {
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
}
