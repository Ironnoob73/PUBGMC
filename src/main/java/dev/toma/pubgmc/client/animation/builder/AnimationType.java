package dev.toma.pubgmc.client.animation.builder;

import dev.toma.pubgmc.client.animation.Animation;
import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.util.object.LazyLoader;
import dev.toma.pubgmc.util.object.Optional;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class AnimationType {

    public final int index;
    private LazyLoader<Condition[]> blockedBy = new LazyLoader<>(() -> new Condition[0]);
    private LazyLoader<Condition[]> overrides = new LazyLoader<>(() -> new Condition[0]);

    public AnimationType(int id) {
        this.index = id;
    }

    public AnimationType blockedBy(Supplier<Condition[]> conditions) {
        this.blockedBy = new LazyLoader<>(conditions);
        return this;
    }

    public AnimationType override(Supplier<Condition[]> overrides) {
        this.overrides = new LazyLoader<>(overrides);
        return this;
    }

    public void apply() {
        if(this.canPlay()) {
            for(Condition condition : overrides.get()) {
                AnimationManager.stopAnimation(condition.type);
            }
        }
    }

    public boolean canPlay() {
        for(Condition condition : blockedBy.get()) {
            Optional<Animation> optional = AnimationManager.getAnimationFromID(condition.type);
            if(!optional.isPresent()) continue;
            Animation animation = optional.get();
            if(!condition.predicate.test(animation)) {
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

    public static class Condition {
        private final AnimationType type;
        private final Predicate<Animation> predicate;

        public Condition(AnimationType type) {
            this(type, animation -> false);
        }

        public Condition(AnimationType type, Predicate<Animation> predicate) {
            this.type = type;
            this.predicate = predicate;
        }
    }
}
