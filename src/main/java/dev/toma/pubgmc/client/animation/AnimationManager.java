package dev.toma.pubgmc.client.animation;

import dev.toma.pubgmc.client.animation.builder.AnimationType;
import dev.toma.pubgmc.util.object.Optional;
import net.minecraftforge.event.TickEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class AnimationManager {

    private static final Map<AnimationType, Animation> ACTIVE_ANIMATIONS = new HashMap<>();

    public static void playNewAnimation(AnimationType type, @Nonnull Animation animation) {
        ACTIVE_ANIMATIONS.put(type, Objects.requireNonNull(animation, "Cannot play null animation!"));
    }

    public static void stopAnimation(AnimationType type) {
        ACTIVE_ANIMATIONS.remove(type);
    }

    public static boolean isAnimationActive(AnimationType type) {
        return ACTIVE_ANIMATIONS.get(type) != null;
    }

    public static boolean isAnimationActive(AnimationType type, Predicate<Animation> condition) {
        Animation animation = ACTIVE_ANIMATIONS.get(type);
        return animation != null && condition.test(animation);
    }

    public static Optional<Animation> getAnimationFromID(AnimationType type) {
        return Optional.of(ACTIVE_ANIMATIONS.get(type));
    }

    public static void animateItemAndHands(float partialTicks) {
        ACTIVE_ANIMATIONS.values().forEach(a -> a.animateItemAndHands(partialTicks));
    }

    public static void animateHands(float partialTicks) {
        ACTIVE_ANIMATIONS.values().forEach(a -> a.animateHands(partialTicks));
    }

    public static void animateRightHand(float partialTicks) {
        ACTIVE_ANIMATIONS.values().forEach(a -> a.animateRightHand(partialTicks));
    }

    public static void animateLeftHand(float partialTicks) {
        ACTIVE_ANIMATIONS.values().forEach(a -> a.animateLeftHand(partialTicks));
    }

    public static void animateItem(float partialTicks) {
        ACTIVE_ANIMATIONS.values().forEach(a -> a.animateItem(partialTicks));
    }

    public static boolean isItemRenderCanceled() {
        for(Animation animation : ACTIVE_ANIMATIONS.values()) {
            if(animation.shouldCancelItemRendering()) return true;
        }
        return false;
    }

    public static void renderTick(float partialTicks, TickEvent.Phase phase) {
        ACTIVE_ANIMATIONS.values().forEach(a -> a.renderTick(partialTicks, phase));
    }

    public static void clientTick() {
        ACTIVE_ANIMATIONS.values().forEach(Animation::clientTick);
    }
}
