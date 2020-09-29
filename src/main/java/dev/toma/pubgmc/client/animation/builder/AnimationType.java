package dev.toma.pubgmc.client.animation.builder;

import dev.toma.pubgmc.client.animation.Animation;
import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.util.object.LazyLoader;
import dev.toma.pubgmc.util.object.Optional;

import java.util.Objects;
import java.util.function.Supplier;

public final class AnimationType {

    public final int index;
    private LazyLoader<AnimationType[]> blockedBy = new LazyLoader<>(() -> new AnimationType[0]);
    private LazyLoader<AnimationType[]> overrides = new LazyLoader<>(() -> new AnimationType[0]);
    private Supplier<Animation> instanceSupplier;

    public AnimationType(int id) {
        this.index = id;
    }

    public AnimationType factory(Supplier<Animation> supplier) {
        this.instanceSupplier = supplier;
        return this;
    }

    public AnimationType blockedBy(Supplier<AnimationType[]> conditions) {
        this.blockedBy = new LazyLoader<>(conditions);
        return this;
    }

    public AnimationType override(Supplier<AnimationType[]> overrides) {
        this.overrides = new LazyLoader<>(overrides);
        return this;
    }

    public Animation getDefaultInstance() {
        if(instanceSupplier == null) {
            throw new UnsupportedOperationException("No defined animation for this type");
        }
        return instanceSupplier.get();
    }

    public void apply() {
        if(this.canPlay()) {
            for(AnimationType type : overrides.get()) {
                AnimationManager.stopAnimation(type);
            }
        }
    }

    public boolean canPlay() {
        for(AnimationType type : blockedBy.get()) {
            Optional<Animation> optional = AnimationManager.getAnimationFromID(type);
            if (optional.isPresent()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimationType that = (AnimationType) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
