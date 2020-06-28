package dev.toma.pubgmc.client.animation.types;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.TickableAnimation;

public class RecoilAnimation extends TickableAnimation {

    public RecoilAnimation() {
        super(5);
    }

    @Override
    public void animateItemAndHands() {
        GlStateManager.rotatef(3.0F * interpolatedValue, 1.0F, 0.0F, 0.0F);
        GlStateManager.translatef(0.0F, 0.0F, 0.1F * interpolatedValue);
    }
}
