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

public class EnergyDrinkItem extends HealingItem {

    public EnergyDrinkItem(String name) {
        super(name, new Properties().group(ITEMS).maxStackSize(3));
    }

    @Override
    public String getSuccessKey() {
        return "pubgmc.heal.energy_drink.success";
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 80;
    }

    @Override
    public void onFinish(PlayerEntity player) {
        PlayerCapHelper.addBoostValue(player, 8);
    }

    @Override
    public Animation getUseAnimation(ItemStack stack) {
        return new UseAnimation(this.getUseDuration(stack));
    }

    @OnlyIn(Dist.CLIENT)
    public static class UseAnimation extends MultiStepAnimation {

        public UseAnimation(int time) {
            super(time);
        }

        @Override
        public void initAnimation() {
            addStep(0F, 0.05F, SimpleAnimation.newSimpleAnimation().rightHand(f -> GlStateManager.translatef(+0.2F * f, -0.2F * f, -0.4F * f)).create());
            addStep(0.05F, 0.3F, SimpleAnimation.newSimpleAnimation().rightHand(f -> GlStateManager.translatef(0.2F, -0.2F - 0.2F * f, -0.4F * f)).leftHand(f -> {
                GlStateManager.translatef(+0.5F * f, +0F * f, -0.5F * f);
                GlStateManager.rotatef(-30F * f, 0.0F, 1.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.2F * f, -0.4F * f, 0.0F);
                GlStateManager.rotatef(+20F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.3F, 0.5F, SimpleAnimation.newSimpleAnimation().rightHand(f -> GlStateManager.translatef(0.2F, -0.4F, -0.4F)).leftHand(f -> {
                GlStateManager.translatef(0.5F - 0.5F * f, 0F, -0.5F + 0.5F * f);
                GlStateManager.rotatef(-30F + 30F * f, 0.0F, 1.0F, 0.0F);
            }).item(f -> {
                GlStateManager.translatef(-0.2F - 0.35F * f, -0.4F - 0.2F * f, +0.3F * f);
                GlStateManager.rotatef(40.0F * f, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotatef(20.0F - 20.0F * f, 0.0F, 0.0F, 1.0F);
            }).create());
            addStep(0.5F, 0.8F, SimpleAnimation.newSimpleAnimation()
                    .rightHand(f -> GlStateManager.translatef(0.2F, -0.4F, -0.4F)).item(f -> {
                        GlStateManager.translatef(-0.55F, -0.6F, 0.3F);
                        GlStateManager.rotatef(40.0F, 1.0F, 0.0F, 0.0F);
                    })
                    .create());
            addStep(0.8F, 1.0F, SimpleAnimation.newSimpleAnimation().rightHand(f -> GlStateManager.translatef(0.2F - 0.2F * f, -0.4F + 0.4F * f, -0.4F + 0.4F * f)).item(f -> {
                GlStateManager.translatef(-0.55F + 0.55F * f, -0.6F + 0.6F * f, 0.3F - 0.3F * f);
                GlStateManager.rotatef(40F - 40F * f, 1.0F, 0.0F, 0.0F);
            }).create());
        }
    }
}
