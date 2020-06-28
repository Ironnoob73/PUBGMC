package dev.toma.pubgmc.client.animation;

import net.minecraftforge.event.TickEvent;

public interface Animation {

    void animateItemAndHands();

    void animateHands();

    void animateRightHand();

    void animateLeftHand();

    void animateItem();

    void clientTick();

    void renderTick(float partialTicks, TickEvent.Phase phase);

    void setAnimationProgress(float value);

    boolean shouldCancelItemRendering();

    boolean isAnimationFinished();
}
