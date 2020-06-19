package dev.toma.pubgmc.client.animation;

public class TickableAnimation extends AnimationFactory {

    protected final int lengthInTicks;
    protected int remainingTicks;

    public TickableAnimation(int lengthInTicks) {
        this.lengthInTicks = lengthInTicks;
        this.remainingTicks = lengthInTicks;
    }

    @Override
    public float getAnimationProgress() {
        return remainingTicks / (float) lengthInTicks;
    }

    @Override
    public void tick() {
        --remainingTicks;
    }

    @Override
    public boolean isAnimationFinished() {
        return remainingTicks <= 0;
    }
}
