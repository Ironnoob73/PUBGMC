package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.client.animation.GunPartAnimation;
import net.minecraftforge.event.TickEvent;

public abstract class GunAnimationFactory implements GunPartAnimation {

    protected float current, prev, interpolated;

    public abstract float getAnimationProgress();

    public abstract void tick();

    @Override
    public final void clientTick() {
        prev = current;
        tick();
    }

    @Override
    public void renderTick(float partialTicks, TickEvent.Phase phase) {
        if(phase == TickEvent.Phase.END) return;
        current = getAnimationProgress();
        interpolated = prev + (current - prev) * partialTicks;
    }

    @Override
    public final boolean shouldCancelItemRendering() {
        return false;
    }

    @Override
    public void setAnimationProgress(float value) {
        this.current = value;
    }

    @Override
    public void animateItemAndHands() {

    }

    @Override
    public void animateHands() {

    }

    @Override
    public void animateRightHand() {

    }

    @Override
    public void animateLeftHand() {

    }

    @Override
    public void animateItem() {

    }

    @Override
    public void animateModel(int path) {

    }
}
