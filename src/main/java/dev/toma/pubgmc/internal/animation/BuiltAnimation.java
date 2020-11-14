package dev.toma.pubgmc.internal.animation;

import dev.toma.pubgmc.client.animation.gun.GunMultiStepAnimation;
import dev.toma.pubgmc.internal.InternalData;

public class BuiltAnimation extends GunMultiStepAnimation {

    public BuiltAnimation(int time) {
        super(time);
    }

    @Override
    public void initAnimation() {
        float f1 = 0.0F;
        for(BuilderAnimationStep step : InternalData.steps) {
            float f2 = f1 + step.length;
            addStep(f1, f2, step.resetState());
            f1 = f2;
        }
    }
}
