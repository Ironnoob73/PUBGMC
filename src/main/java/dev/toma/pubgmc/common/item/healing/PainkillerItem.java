package dev.toma.pubgmc.common.item.healing;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.capability.player.PlayerCapHelper;
import dev.toma.pubgmc.client.animation.Animation;
import dev.toma.pubgmc.client.animation.MultiStepAnimation;
import dev.toma.pubgmc.client.animation.SimpleAnimation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PainkillerItem extends HealingItem {

    public PainkillerItem(String name) {
        super(name, new Properties().group(ITEMS).maxStackSize(3));
    }

    @Override
    public String getSuccessKey() {
        return "pubgmc.heal.painkillers.success";
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 120;
    }

    @Override
    public void onFinish(PlayerEntity player) {
        PlayerCapHelper.addBoostValue(player, 12);
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
            /*addStep(0F, 0.1F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F * f)).rightHand(f -> GlStateManager.translatef(+0.2F * f, -0.3F * f, -0.4F * f)).leftHand(f -> GlStateManager.translatef(0.0F, 0.0F, +0.2F * f)).create());
            addStep(0.1F, 0.3F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).rightHand(f -> GlStateManager.translatef(0.2F, -0.3F, -0.4F)).leftHand(f -> {
                GlStateManager.translatef(+0.1F * f, -0.3F * f, 0.2F - 0.6F * f);
                GlStateManager.rotatef(-30F * f, 0.0F, 1.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.45F * f, -0.8F * f, 0.0F);
                GlStateManager.rotatef(+60F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.3F, 0.4F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).rightHand(f -> GlStateManager.translatef(0.2F - 0.05F * f, -0.3F + 0.15F * f, -0.4F)).leftHand(f -> {
                GlStateManager.translatef(0.1F, -0.3F, -0.4F);
                GlStateManager.rotatef(-30F, 0.0F, 1.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.45F + 0.1F * f, -0.8F - 0.1F * f, 0.0F);
                GlStateManager.rotatef(60F + 20F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.4F, 0.5F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).rightHand(f -> GlStateManager.translatef(0.15F + 0.05F * f, -0.15F - 0.15F * f, -0.4F)).leftHand(f -> {
                GlStateManager.translatef(0.1F, -0.3F, -0.4F);
                GlStateManager.rotatef(-30F, 0.0F, 1.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.35F, -0.9F, 0.0F);
                GlStateManager.rotatef(80F - 20F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.5F, 0.7F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).rightHand(f -> GlStateManager.translatef(0.2F, -0.3F - 0.8F * f, -0.4F + 2F * f)).leftHand(f -> {
                GlStateManager.translatef(0.1F + 0.2F * f, -0.3F + 0.1F * f, -0.4F + 0.4F * f);
                GlStateManager.rotatef(-30F + 30F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(+50F * f, 1.0F, 0.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.45F + 0.45F * f, -0.8F + 0.2F * f, +0.6F * f);
                GlStateManager.rotatef(60F - 60F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.7F, 1F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.3F)).rightHand(f -> GlStateManager.translatef(0.2F, -1.1F, 1.6F)).leftHand(f -> {
                GlStateManager.translatef(0.3F - 0.3F * f, -0.2F + 0.2F * f, -0F + 0.6F * f);
                GlStateManager.rotatef(0.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(50F - 50F * f, 1.0F, 0.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(0F, -0.6F, 0.6F);
                GlStateManager.rotatef(0.0F, 0.0F, 0.0F, 1.0F);
            }).create());*/
            addStep(0F, 0.1F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.2F * f)).rightHand(f -> GlStateManager.translatef(+0.15F * f, -0.3F * f, -0.3F * f)).leftHand(f -> GlStateManager.translatef(0.0F, -0.2F * f, 0.0F)).create());
            addStep(0.1F, 0.3F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.2F)).rightHand(f -> GlStateManager.translatef(0.15F, -0.3F, -0.3F)).leftHand(f -> {
                GlStateManager.translatef(+0.2F * f, -0.2F - 0.1F * f, -0.4F * f);
                GlStateManager.rotatef(-40F * f, 0.0F, 1.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.4F * f, -0.8F * f, 0.0F);
                GlStateManager.rotatef(+60F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.3F, 0.45F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.2F)).rightHand(f -> GlStateManager.translatef(0.15F, -0.3F + 0.1F * f, -0.3F)).leftHand(f -> {
                GlStateManager.translatef(0.2F, -0.3F, -0.4F);
                GlStateManager.rotatef(-40F, 0.0F, 1.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.4F + 0.1F * f, -0.8F - 0.1F * f, 0.0F);
                GlStateManager.rotatef(60F + 20F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.45F, 0.6F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.2F)).rightHand(f -> GlStateManager.translatef(0.15F, -0.2F - 0.1F * f, -0.3F)).leftHand(f -> {
                GlStateManager.translatef(0.2F, -0.3F, -0.4F);
                GlStateManager.rotatef(-40F, 0.0F, 1.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.3F, -0.9F + 0.1F * f, 0.0F);
                GlStateManager.rotatef(80F - 20F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.6F, 0.7F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.2F)).rightHand(f -> GlStateManager.translatef(0.15F, -0.3F, -0.3F + 0.2F * f)).leftHand(f -> {
                GlStateManager.translatef(0.2F, -0.3F, -0.4F);
                GlStateManager.rotatef(-40F + 20F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(+40F * f, 1.0F, 0.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.3F + 0.3F * f, -0.8F + 0.2F * f, 0.0F);
                GlStateManager.rotatef(60F - 60F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.7F, 0.8F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.2F)).rightHand(f -> GlStateManager.translatef(0.15F, -0.3F, -0.1F)).leftHand(f -> {
                GlStateManager.translatef(0.2F + 0.1F * f, -0.3F + 0.1F * f, -0.4F + 0.3F * f);
                GlStateManager.rotatef(-20F + 20F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(40F + 20F * f, 1.0F, 0.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(0.0F, -0.6F, 0.0F);
                GlStateManager.rotatef(0.0F, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.8F, 1F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0.2F)).rightHand(f -> GlStateManager.translatef(0.15F, -0.3F, -0.1F)).leftHand(f -> {
                GlStateManager.translatef(0.3F - 0F * f, -0.2F + 0.1F * f, -0.1F - 0F * f);
                GlStateManager.rotatef(0.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(60F - 60F * f, 1.0F, 0.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(0.0F, -0.6F, 0.0F);
                GlStateManager.rotatef(0.0F, 0.0F, 0.0F, 1.0F);
            }).create());

        }
    }
}
