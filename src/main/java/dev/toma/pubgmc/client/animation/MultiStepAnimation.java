package dev.toma.pubgmc.client.animation;

import dev.toma.pubgmc.util.object.Pair;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiStepAnimation extends TickableAnimation {

    protected final List<Pair<Range, Animation>> stepList;
    protected Pair<Range, Animation> current;
    protected int index = 0;

    public MultiStepAnimation(int length) {
        super(length);
        this.stepList = new ArrayList<>();
        this.initAnimation();
        if(stepList.isEmpty()) {
            throw new IllegalArgumentException("Animation step list must contain atleast 1 step!");
        }
        this.current = stepList.get(0);
    }

    public abstract void initAnimation();

    public void addStep(float min, float max, Animation animation) {
        this.addStep(new Range(min, max), animation);
    }

    public void addStep(Range range, Animation animation) {
        this.stepList.add(Pair.of(range, animation));
    }

    public void onStepChanged() {
    }

    public boolean isAtLastStep() {
        return index == stepList.size() - 1;
    }

    public List<Pair<Range, Animation>> getStepList() {
        return stepList;
    }

    @Override
    public void animateItemAndHands(float partialTicks) {
        current.getRight().animateItemAndHands(partialTicks);
    }

    @Override
    public void animateHands(float partialTicks) {
        current.getRight().animateHands(partialTicks);
    }

    @Override
    public void animateRightHand(float partialTicks) {
        current.getRight().animateRightHand(partialTicks);
    }

    @Override
    public void animateLeftHand(float partialTicks) {
        current.getRight().animateLeftHand(partialTicks);
    }

    @Override
    public void animateItem(float partialTicks) {
        current.getRight().animateItem(partialTicks);
    }

    @Override
    public void renderTick(float partialTicks, TickEvent.Phase phase) {
        current.getRight().renderTick(partialTicks, phase);
    }

    @Override
    public void tick() {
        super.tick();
        float progress = 1.0F - getAnimationProgress();
        Range range = current.getLeft();
        Animation animation = current.getRight();
        if(range.isInRange(progress)) {
            animation.clientTick();
            float stepProgress = range.getProgress(progress);
            animation.setAnimationProgress(stepProgress);
        } else if(range.isOutsideRange(progress)) {
            if(!isAtLastStep()) {
                ++index;
                current = stepList.get(index);
                onStepChanged();
            }
        }
    }

    @Override
    public boolean shouldCancelItemRendering() {
        return current.getRight().shouldCancelItemRendering();
    }

    public int getIndex() {
        return index;
    }

    public static class Range {

        protected final float min, max;

        public Range(float min, float max) {
            this.min = min;
            this.max = max;
        }

        public float getProgress(float value) {
            return (value - min) / (max - min);
        }

        public boolean isInRange(float value) {
            return value > min && value <= max;
        }

        public boolean isOutsideRange(float value) {
            return value >= max;
        }

        @Override
        public String toString() {
            return "Range{" +
                    "min=" + min +
                    ", max=" + max +
                    '}';
        }
    }
}
