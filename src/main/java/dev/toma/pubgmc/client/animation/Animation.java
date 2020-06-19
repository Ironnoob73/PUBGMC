package dev.toma.pubgmc.client.animation;

import net.minecraftforge.event.TickEvent;

public interface Animation {

    void animateItemAndHands(float partialTicks);

    void animateHands(float partialTicks);

    void animateRightHand(float partialTicks);

    void animateLeftHand(float partialTicks);

    void animateItem(float partialTicks);

    void clientTick();

    void renderTick(float partialTicks, TickEvent.Phase phase);

    void setAnimationProgress(float value);

    boolean shouldCancelItemRendering();

    boolean isAnimationFinished();
}
