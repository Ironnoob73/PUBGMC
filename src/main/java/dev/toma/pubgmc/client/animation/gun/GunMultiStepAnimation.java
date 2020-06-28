package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.client.animation.Animation;
import dev.toma.pubgmc.client.animation.GunPartAnimation;
import dev.toma.pubgmc.client.animation.MultiStepAnimation;
import dev.toma.pubgmc.util.object.Pair;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class GunMultiStepAnimation extends GunTickableAnimation {

    protected final List<Pair<MultiStepAnimation.Range, GunPartAnimation>> stepList;
    protected Pair<MultiStepAnimation.Range, GunPartAnimation> current;
    protected int index;

    public GunMultiStepAnimation(int lengthInTicks) {
        super(lengthInTicks);
        stepList = new ArrayList<>();
        initAnimation();
        if(stepList.isEmpty()) {
            throw new IllegalArgumentException("Animation step list must contain atleast 1 step!");
        }
        this.current = stepList.get(0);
    }

    public abstract void initAnimation();

    public void addStep(float min, float max, GunPartAnimation animation) {
        this.addStep(new MultiStepAnimation.Range(min, max), animation);
    }

    public void addStep(MultiStepAnimation.Range range, GunPartAnimation animation) {
        this.stepList.add(Pair.of(range, animation));
    }

    public void onStepChanged() {
    }

    public boolean isAtLastStep() {
        return index == stepList.size() - 1;
    }

    public List<Pair<MultiStepAnimation.Range, GunPartAnimation>> getStepList() {
        return stepList;
    }

    @Override
    public void animateItemAndHands() {
        current.getRight().animateItemAndHands();
    }

    @Override
    public void animateHands() {
        current.getRight().animateHands();
    }

    @Override
    public void animateRightHand() {
        current.getRight().animateRightHand();
    }

    @Override
    public void animateLeftHand() {
        current.getRight().animateLeftHand();
    }

    @Override
    public void animateItem() {
        current.getRight().animateItem();
    }

    @Override
    public void animateModel(int path) {
        current.getRight().animateModel(path);
    }

    @Override
    public void renderTick(float partialTicks, TickEvent.Phase phase) {
        current.getRight().renderTick(partialTicks, phase);
    }

    @Override
    public void tick() {
        super.tick();
        float progress = 1.0F - getAnimationProgress();
        MultiStepAnimation.Range range = current.getLeft();
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

    public int getIndex() {
        return index;
    }
}
