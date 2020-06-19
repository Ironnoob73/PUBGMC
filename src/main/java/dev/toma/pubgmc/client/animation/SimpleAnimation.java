package dev.toma.pubgmc.client.animation;

import dev.toma.pubgmc.util.object.Optional;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;

import java.util.Objects;
import java.util.function.Consumer;

public class SimpleAnimation implements Animation {

    private float c, p, s;
    private final Optional<Consumer<Float>> itemAndHandAnimation = Optional.empty();
    private final Optional<Consumer<Float>> handAnimation = Optional.empty();
    private final Optional<Consumer<Float>> rightHandAnimation = Optional.empty();
    private final Optional<Consumer<Float>> leftHandAnimation = Optional.empty();
    private final Optional<Consumer<Float>> itemAnimation = Optional.empty();

    private SimpleAnimation(Builder builder) {
        itemAndHandAnimation.map(builder.itemAndHand);
        handAnimation.map(builder.hand);
        rightHandAnimation.map(builder.rightHand);
        leftHandAnimation.map(builder.leftHand);
        itemAnimation.map(builder.item);
    }

    @Override
    public void animateItemAndHands(float partialTicks) {
        itemAndHandAnimation.ifPresent(a -> a.accept(s));
    }

    @Override
    public void animateHands(float partialTicks) {
        handAnimation.ifPresent(a -> a.accept(s));
    }

    @Override
    public void animateRightHand(float partialTicks) {
        rightHandAnimation.ifPresent(a -> a.accept(s));
    }

    @Override
    public void animateLeftHand(float partialTicks) {
        leftHandAnimation.ifPresent(a -> a.accept(s));
    }

    @Override
    public void animateItem(float partialTicks) {
        itemAnimation.ifPresent(a -> a.accept(s));
    }

    @Override
    public void clientTick() {
        p = c;
    }

    @Override
    public boolean isAnimationFinished() {
        return false;
    }

    @Override
    public boolean shouldCancelItemRendering() {
        return false;
    }

    @Override
    public void setAnimationProgress(float value) {
        this.c = value;
    }

    @Override
    public void renderTick(float partialTicks, TickEvent.Phase phase) {
        if(phase == TickEvent.Phase.START) {
            s = p + (c - p) * partialTicks;
        }
    }

    public static Builder newSimpleAnimation() {
        return new Builder();
    }

    public static class Builder {

        private Consumer<Float> itemAndHand;
        private Consumer<Float> hand;
        private Consumer<Float> rightHand;
        private Consumer<Float> leftHand;
        private Consumer<Float> item;

        private Builder() {
        }

        public Builder itemHand(Consumer<Float> consumer) {
            this.itemAndHand = consumer;
            return this;
        }

        public Builder hand(Consumer<Float> consumer) {
            this.hand = consumer;
            return this;
        }

        public Builder rightHand(Consumer<Float> consumer) {
            this.rightHand = consumer;
            return this;
        }

        public Builder leftHand(Consumer<Float> consumer) {
            this.leftHand = consumer;
            return this;
        }

        public Builder item(Consumer<Float> consumer) {
            this.item = consumer;
            return this;
        }

        public SimpleAnimation create() {
            Objects.requireNonNull(itemAndHand);
            Objects.requireNonNull(hand);
            Objects.requireNonNull(rightHand);
            Objects.requireNonNull(leftHand);
            Objects.requireNonNull(item);
            return new SimpleAnimation(this);
        }
    }
}
