package dev.toma.pubgmc.client.animation.types;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.MultiStepAnimation;
import dev.toma.pubgmc.client.animation.SimpleAnimation;

public class FiremodeAnimation extends MultiStepAnimation {

    public FiremodeAnimation() {
        super(5);
    }

    @Override
    public void initAnimation() {
        addStep(0F, 0.5F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
            GlStateManager.translatef(-0.05F * f, 0.0F, +0.05F * f);
            GlStateManager.rotatef(-2F * f, 1.0F, 0.0F, 0.0F);
        }).create());
        addStep(0.5F, 1F, SimpleAnimation.newSimpleAnimation().itemHand(f -> {
            GlStateManager.translatef(-0.05F + 0.05F * f, 0.0F, 0.05F - 0.05F * f);
            GlStateManager.rotatef(-2F + 2F * f, 1.0F, 0.0F, 0.0F);
        }).create());

    }
}
