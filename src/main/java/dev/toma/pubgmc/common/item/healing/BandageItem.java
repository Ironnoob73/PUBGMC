package dev.toma.pubgmc.common.item.healing;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.Animation;
import dev.toma.pubgmc.client.animation.MultiStepAnimation;
import dev.toma.pubgmc.client.animation.SimpleAnimation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BandageItem extends HealingItem {

    public BandageItem(String name) {
        super(name, new Properties().group(ITEMS).maxStackSize(5));
    }

    @Override
    public String getSuccessKey() {
        return "pubgmc.heal.bandage.success";
    }

    @Override
    public String getFailKey() {
        return "pubgmc.heal.bandage.fail";
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 40;
    }

    @Override
    public void onFinish(PlayerEntity player) {
        player.setHealth(Math.min(15, player.getHealth() + 3));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getHealth() < 15;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Animation getUseAnimation(ItemStack stack) {
        return new UseAnimation(this.getUseDuration(stack));
    }

    @OnlyIn(Dist.CLIENT)
    public static class UseAnimation extends MultiStepAnimation {

        public UseAnimation(int ticks) {
            super(ticks);
        }

        @Override
        public void initAnimation() {
            addStep(0F, 0.1F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.rotatef(+50F * f, 0.0F, 1.0F, 0.0F)).leftHand(f -> {
                GlStateManager.translatef(-0.1F * f, -0.2F * f, -1.3F * f);
                GlStateManager.rotatef(-50F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(+10F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(+110F * f, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.8F * f, 0.0F, -0.8F * f);
                GlStateManager.rotatef(-50F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(+10F * f, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.1F, 0.2F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.rotatef(50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-10F * f, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(-0.1F, -0.2F + 0.2F * f, -1.3F - 0.1F * f);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(110F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.8F, 0.0F, -0.8F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.2F, 0.35F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(+0.3F * f, 0.0F, 0.0F);
                GlStateManager.rotatef(50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
            }).rightHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F * f)).leftHand(f -> {
                GlStateManager.translatef(-0.1F - 0.3F * f, 0.0F, -1.4F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(110F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.8F - 0.4F * f, 0.0F, -0.8F - 0F * f);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.35F, 0.45F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(0.3F, +0.1F * f, 0.0F);
                GlStateManager.rotatef(50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
            }).rightHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).leftHand(f -> {
                GlStateManager.translatef(-0.4F, -0.1F * f, -1.4F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(110F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-1.2F, 0.0F, -0.8F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.45F, 0.55F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(0.3F, 0.1F - 0.1F * f, 0.0F);
                GlStateManager.rotatef(50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
            }).rightHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).leftHand(f -> {
                GlStateManager.translatef(-0.4F, -0.1F + 0.1F * f, -1.4F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(110F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-1.2F, 0.0F, -0.8F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.55F, 0.65F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(0.3F, +0.1F * f, 0.0F);
                GlStateManager.rotatef(50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
            }).rightHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).leftHand(f -> {
                GlStateManager.translatef(-0.4F, -0.1F * f, -1.4F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(110F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-1.2F, 0.0F, -0.8F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.65F, 0.75F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(0.3F, 0.1F - 0.1F * f, 0.0F);
                GlStateManager.rotatef(50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
            }).rightHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).leftHand(f -> {
                GlStateManager.translatef(-0.4F, -0.1F + 0.1F * f, -1.4F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(110F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-1.2F, 0.0F, -0.8F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.75F, 0.85F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(0.3F, +0.1F * f, 0.0F);
                GlStateManager.rotatef(50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
            }).rightHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).leftHand(f -> {
                GlStateManager.translatef(-0.4F, -0.1F * f, -1.4F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(110F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-1.2F, 0.0F, -0.8F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.85F, 0.9F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(0.3F, 0.1F + 0.2F * f, 0.0F);
                GlStateManager.rotatef(50F - 20F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-10F - 10F * f, 1.0F, 0.0F, 0.0F);
            }).rightHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).leftHand(f -> {
                GlStateManager.translatef(-0.4F - 0.4F * f, -0.1F, -1.4F + 0.5F * f);
                GlStateManager.rotatef(-50F + 20F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F + 10F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(110F - 50F * f, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-1.2F, 0.0F, -0.8F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
            }).create());
            addStep(0.9F, 1F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(0.3F, 0.3F - 1F * f, 0.0F);
                GlStateManager.rotatef(30F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(-20F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(+40F * f, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).leftHand(f -> {
                GlStateManager.translatef(-0.8F, -0.1F, -0.9F);
                GlStateManager.rotatef(-30F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(20F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(60F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-1.2F, 0.0F, -0.8F);
                GlStateManager.rotatef(-50F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(10F, 1.0F, 0.0F, 0.0F);
            }).create());

        }
    }
}
