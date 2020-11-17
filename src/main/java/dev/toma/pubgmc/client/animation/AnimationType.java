package dev.toma.pubgmc.client.animation;

import dev.toma.pubgmc.util.object.Optional;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Objects;
import java.util.function.Function;

public final class AnimationType {

    public final int index;
    private AnimationType[] blockedBy = new AnimationType[0];
    private AnimationType[] overrides = new AnimationType[0];
    private Function<PlayerEntity, Animation> factory;

    public AnimationType(int id) {
        this.index = id;
    }

    public AnimationType factory(Function<PlayerEntity, Animation> factory) {
        this.factory = factory;
        return this;
    }

    public AnimationType blockedBy(AnimationType... conditions) {
        this.blockedBy = conditions;
        return this;
    }

    public AnimationType override(AnimationType... overrides) {
        this.overrides = overrides;
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
            for(AnimationType type : overrides) {
                AnimationManager.stopAnimation(type);
            }
        }
    }

    public boolean canPlay() {
        for(AnimationType type : blockedBy) {
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
