package dev.toma.pubgmc.client.animation.types;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.AnimationFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class SprintAnimation extends AnimationFactory {

    @Override
    public float getAnimationProgress() {
        return currentValue;
    }

    @Override
    public void tick() {
        float speed = 0.15F;
        PlayerEntity player = Minecraft.getInstance().player;
        if(player.isSprinting() && currentValue < 1.0F) {
            currentValue = Math.min(1.0F, currentValue + speed);
        } else if(!player.isSprinting() && currentValue > 0.0F) {
            currentValue = Math.max(0.0F, currentValue - speed);
        }
    }

    @Override
    public boolean isAnimationFinished() {
        return !Minecraft.getInstance().player.isSprinting() && currentValue <= 0.0F;
    }

    @Override
    public void animateItemAndHands() {
        GlStateManager.rotatef(-20.0F * interpolatedValue, 1.0F, 0.0F, 0.0F);
    }

    @Override
    public void animateLeftHand() {
        GlStateManager.translatef(0.0F, -0.5F * interpolatedValue, 0.8F * interpolatedValue);
        GlStateManager.rotatef(40.0F * interpolatedValue, 0.0F, 1.0F, 0.0F);
    }
}
