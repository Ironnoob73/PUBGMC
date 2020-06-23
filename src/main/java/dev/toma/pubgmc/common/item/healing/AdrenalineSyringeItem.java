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

public class AdrenalineSyringeItem extends HealingItem {

    public AdrenalineSyringeItem(String name) {
        super(name, new Properties().group(ITEMS).maxStackSize(1));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 120;
    }

    @Override
    public String getSuccessKey() {
        return "pubgmc.heal.adrenaline_syringe.success";
    }

    @Override
    public void onFinish(PlayerEntity player) {
        PlayerCapHelper.addBoostValue(player, 20);
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
            addStep(0F, 0.2F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(0.0F, 0.0F, -0F * f)).item(f -> {
                GlStateManager.translatef(-0.2F * f, -1F * f, +0.6F * f);
                GlStateManager.rotatef(+100F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.2F, 0.4F, SimpleAnimation.newSimpleAnimation().itemHand(f -> GlStateManager.translatef(+0.1F * f, -0.1F * f, -0F - 0.7F * f)).leftHand(f -> {
                GlStateManager.translatef(-0.5F * f, -0.5F * f, 0.0F);
                GlStateManager.rotatef(-10F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(+30F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-30F * f, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.2F, -1F, 0.6F);
                GlStateManager.rotatef(100F, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.4F, 0.63F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(0.1F - 0.2F * f, -0.1F - 0.1F * f, -0.7F);
                GlStateManager.rotatef(-10F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(+30F * f, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, -0.1F * f);
                GlStateManager.rotatef(-20F * f, 0.0F, 1.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(-0.5F + 0.6F * f, -0.5F + 0.1F * f, -0.4F * f);
                GlStateManager.rotatef(-10F - 10F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(30F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-30F - 50F * f, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.2F - 0.1F * f, -1F + 0.2F * f, 0.6F - 0.1F * f);
                GlStateManager.rotatef(100F - 20F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.63F, 0.65F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(-0.1F - 0.3F * f, -0.2F - 0.1F * f, -0.7F);
                GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(30F, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, -0.1F);
                GlStateManager.rotatef(-20F + 20F * f, 0.0F, 1.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.1F + 0.2F * f, -0.4F, -0.4F);
                GlStateManager.rotatef(-20F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(30F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-80F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.3F, -0.8F, 0.5F);
                GlStateManager.rotatef(80F, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.65F, 0.85F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(-0.4F, -0.3F, -0.7F);
                GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(30F, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, -0.1F + 0.3F * f);
                GlStateManager.rotatef(0.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(+20F * f, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.3F, -0.4F, -0.4F);
                GlStateManager.rotatef(-20F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(30F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-80F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.3F, -0.8F, 0.5F);
                GlStateManager.rotatef(80F, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.85F, 0.9F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(-0.4F + 0.8F * f, -0.3F + 0.2F * f, -0.7F - 0.1F * f);
                GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(30F, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(0.0F, 0.0F, 0.2F);
                GlStateManager.rotatef(0.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(20F, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(0.3F - 0.9F * f, -0.4F + 0.3F * f, -0.4F + 0.2F * f);
                GlStateManager.rotatef(-20F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(30F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-80F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.3F, -0.8F, 0.5F);
                GlStateManager.rotatef(80F, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.9F, 1F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
                GlStateManager.translatef(0.4F, -0.1F, -0.8F);
                GlStateManager.rotatef(-10F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(30F, 0.0F, 0.0F, 1.0F);
            }).rightHand(f -> {
                GlStateManager.translatef(-0.3F * f, +0F * f, 0.2F + 0.8F * f);
                GlStateManager.rotatef(-50F * f, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(20F - 20F * f, 1.0F, 0.0F, 0.0F);
            }).leftHand(f -> {
                GlStateManager.translatef(-0.6F, -0.1F, -0.2F);
                GlStateManager.rotatef(-20F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotatef(30F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(-80F, 0.0F, 0.0F, 1.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.3F + 1.6F * f, -0.8F - 0.9F * f, 0.5F + 0.7F * f);
                GlStateManager.rotatef(80F - 30F * f, 0.0F, 0.0F, 1.0F);
            }).create());
        }
    }
}
