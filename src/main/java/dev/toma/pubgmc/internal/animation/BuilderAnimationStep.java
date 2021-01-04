package dev.toma.pubgmc.internal.animation;

import dev.toma.pubgmc.client.animation.Animation;
import dev.toma.pubgmc.client.animation.GunPartAnimation;
import dev.toma.pubgmc.client.animation.MultiStepAnimation;
import dev.toma.pubgmc.internal.InternalData;
import net.minecraftforge.event.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class BuilderAnimationStep implements GunPartAnimation {

    public float length = 1.0F;
    public Map<IAnimationPart, AnimationData> map = new HashMap<>();
    protected float current, prev, smooth;

    public BuilderAnimationStep(BuilderAnimationStep other) {
        for(IAnimationPart part : IAnimationPart.PARTS) {
            AnimationData prev = other.map.get(part);
            map.put(part, new AnimationData(prev));
        }
    }

    public BuilderAnimationStep() {
        for(IAnimationPart part : IAnimationPart.PARTS) {
            map.put(part, new AnimationData());
        }
    }

    public BuilderAnimationStep resetState() {
        this.current = 0.0F;
        this.prev = 0.0F;
        this.smooth = 0.0F;
        return this;
    }

    public Animation asTempAnimation() {
        int i = (int)(InternalData.animationLength * length);
        return new MultiStepAnimation(i) {
            @Override
            public void initAnimation() {
                addStep(0.0F, 1.0F, BuilderAnimationStep.this.resetState());
            }
        };
    }

    public int getIntValue() {
        return (int)(length * 100.0F);
    }

    public void updateValue(float value) {
        map.get(InternalData.part).update(InternalData.context, InternalData.axis, value);
    }

    @Override
    public void animateItemAndHands() {
        map.get(IAnimationPart.ITEM_AND_HANDS).apply(smooth);
    }

    @Override
    public void animateHands() {
        map.get(IAnimationPart.HANDS).apply(smooth);
    }

    @Override
    public void animateRightHand() {
        map.get(IAnimationPart.RIGHT_HAND).apply(smooth);
    }

    @Override
    public void animateLeftHand() {
        map.get(IAnimationPart.LEFT_HAND).apply(smooth);
    }

    @Override
    public void animateItem() {
        map.get(IAnimationPart.ITEM).apply(smooth);
    }

    @Override
    public void animateModel(int path) {
        AnimationData data = map.get(new IAnimationPart.Model(path));
        if(data != null) {
            data.apply(smooth);
        }
    }

    @Override
    public void clientTick() {
        this.prev = this.current;
    }

    @Override
    public void renderTick(float partialTicks, TickEvent.Phase phase) {
        if(phase == TickEvent.Phase.START) this.interpolate(partialTicks);
    }

    @Override
    public boolean isAnimationFinished() {
        return false;
    }

    @Override
    public void setAnimationProgress(float progress) {
        this.current = progress;
    }

    private void interpolate(float partialTicks) {
        this.smooth = this.prev + (this.current - this.prev) * partialTicks;
    }

    @Override
    public boolean shouldCancelItemRendering() {
        return false;
    }
}
