package dev.toma.pubgmc.client.animation.gun;

public class GunTickableAnimation extends GunAnimationFactory {

    private final int lengthInTicks;
    private int remainingTicks;

    public GunTickableAnimation(int lengthInTicks) {
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
