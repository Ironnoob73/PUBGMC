package dev.toma.pubgmc.client.animation;

import net.minecraftforge.event.TickEvent;

public abstract class AnimationFactory implements Animation {

    protected float currentValue;
    protected float previousValue;
    protected float interpolatedValue;

    public abstract float getAnimationProgress();

    public abstract void tick();

    @Override
    public final void clientTick() {
        this.previousValue = this.currentValue;
        this.tick();
    }

    @Override
    public void renderTick(float partialTicks, TickEvent.Phase phase) {
        if(phase == TickEvent.Phase.END) return;
        this.currentValue = this.getAnimationProgress();
        this.interpolatedValue = this.previousValue + (this.currentValue - this.previousValue) * partialTicks;
    }

    @Override
    public boolean shouldCancelItemRendering() {
        return false;
    }

    @Override
    public void setAnimationProgress(float value) {
        this.currentValue = value;
    }

    @Override
    public void animateItemAndHands(float partialTicks) {

    }

    @Override
    public void animateHands(float partialTicks) {

    }

    @Override
    public void animateRightHand(float partialTicks) {

    }

    @Override
    public void animateLeftHand(float partialTicks) {

    }

    @Override
    public void animateItem(float partialTicks) {

    }
}
