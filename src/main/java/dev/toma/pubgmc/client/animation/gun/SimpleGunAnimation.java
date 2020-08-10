package dev.toma.pubgmc.client.animation.gun;

import dev.toma.pubgmc.client.animation.GunPartAnimation;
import dev.toma.pubgmc.util.object.Optional;
import net.minecraftforge.event.TickEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SimpleGunAnimation implements GunPartAnimation {

    private float c, p, s;
    private final Optional<Consumer<Float>> itemAndHandAnimation = Optional.empty();
    private final Optional<Consumer<Float>> handAnimation = Optional.empty();
    private final Optional<Consumer<Float>> rightHandAnimation = Optional.empty();
    private final Optional<Consumer<Float>> leftHandAnimation = Optional.empty();
    private final Optional<Consumer<Float>> itemAnimation = Optional.empty();
    private final Optional<BiConsumer<Integer, Float>> modelAnimation = Optional.empty();

    private SimpleGunAnimation(Builder builder) {
        itemAndHandAnimation.map(builder.itemAndHand);
        handAnimation.map(builder.hand);
        rightHandAnimation.map(builder.rightHand);
        leftHandAnimation.map(builder.leftHand);
        itemAnimation.map(builder.item);
        modelAnimation.map(builder.model);
    }

    @Override
    public void animateItemAndHands() {
        itemAndHandAnimation.ifPresent(a -> a.accept(s));
    }

    @Override
    public void animateHands() {
        handAnimation.ifPresent(a -> a.accept(s));
    }

    @Override
    public void animateRightHand() {
        rightHandAnimation.ifPresent(a -> a.accept(s));
    }

    @Override
    public void animateLeftHand() {
        leftHandAnimation.ifPresent(a -> a.accept(s));
    }

    @Override
    public void animateItem() {
        itemAnimation.ifPresent(a -> a.accept(s));
    }

    @Override
    public void animateModel(int path) {
        modelAnimation.ifPresent(a -> a.accept(path, s));
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

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private Consumer<Float> itemAndHand;
        private Consumer<Float> hand;
        private Consumer<Float> rightHand;
        private Consumer<Float> leftHand;
        private Consumer<Float> item;
        private BiConsumer<Integer, Float> model;

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

        public Builder model(BiConsumer<Integer, Float> consumer) {
            this.model = consumer;
            return this;
        }

        public SimpleGunAnimation create() {
            return new SimpleGunAnimation(this);
        }
    }
}
