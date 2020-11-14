package dev.toma.pubgmc.internal.animation;

import dev.toma.pubgmc.internal.InternalData;

public class AnimationData {

    public IContext.Translation translation;
    public IContext.Rotation rotation;

    public AnimationData() {
        this.translation = new IContext.Translation();
        this.rotation = new IContext.Rotation();
    }

    public AnimationData(AnimationData other) {
        this.translation = new IContext.Translation(other.translation);
        this.rotation = new IContext.Rotation(other.rotation);
    }

    public void update(InternalData.Context context, InternalData.Axis axis, float f) {
        if(context == InternalData.Context.TRANSLATION)
            translation.update(axis, f);
        else
            rotation.update(axis, f);
    }

    public void apply(float smooth) {
        translation.apply(smooth);
        rotation.apply(smooth);
    }

    public boolean isEmpty() {
        return translation.isEmpty() && rotation.isEmpty();
    }
}
