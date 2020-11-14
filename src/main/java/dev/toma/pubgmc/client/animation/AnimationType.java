package dev.toma.pubgmc.client.animation;

import dev.toma.pubgmc.util.object.LazyLoader;
import dev.toma.pubgmc.util.object.Optional;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class AnimationType {

    public final int index;
    private LazyLoader<AnimationType[]> blockedBy = new LazyLoader<>(() -> new AnimationType[0]);
    private LazyLoader<AnimationType[]> overrides = new LazyLoader<>(() -> new AnimationType[0]);
    private Function<PlayerEntity, Animation> factory;

    public AnimationType(int id) {
        this.index = id;
    }

    public AnimationType factory(Function<PlayerEntity, Animation> factory) {
        this.factory = factory;
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

    public Animation getDefaultInstance(PlayerEntity player) {
        if(factory == null) {
            throw new UnsupportedOperationException("No defined animation for this type");
        }
        return factory.apply(player);
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
