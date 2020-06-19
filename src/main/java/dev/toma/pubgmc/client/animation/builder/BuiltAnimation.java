package dev.toma.pubgmc.client.animation.builder;

import dev.toma.pubgmc.client.animation.MultiStepAnimation;

public class BuiltAnimation extends MultiStepAnimation {

    public BuiltAnimation(int time) {
        super(time);
    }

    @Override
    public void initAnimation() {
        float f1 = 0.0F;
        for(BuilderAnimationStep step : BuilderData.steps) {
            float f2 = f1 + step.length;
            addStep(f1, f2, step.resetState());
            f1 = f2;
        }
    }
}
