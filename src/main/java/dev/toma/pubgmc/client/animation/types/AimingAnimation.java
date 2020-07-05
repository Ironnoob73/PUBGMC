package dev.toma.pubgmc.client.animation.types;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.capability.player.AimInfo;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.client.animation.AnimationFactory;
import dev.toma.pubgmc.util.object.Optional;
import net.minecraft.client.Minecraft;

import javax.vecmath.Vector3f;
import java.util.function.Consumer;

public class AimingAnimation extends AnimationFactory {

    private final AimInfo aimInfo;
    private final Optional<Vector3f> scopeOffset = Optional.empty();
    private final Vector3f itemVec;
    private Consumer<Float> leftHandRender = f -> {};
    private Consumer<Float> rightHandRender = f -> {};

    public AimingAnimation(float x, float y, float z) {
        this(new Vector3f(x, y, z));
    }

    public AimingAnimation(Vector3f item) {
        aimInfo = PlayerCapFactory.get(Minecraft.getInstance().player).getAimInfo();
        this.itemVec = item;
    }

    public AimingAnimation left(Consumer<Float> consumer) {
        leftHandRender = consumer;
        return this;
    }

    public AimingAnimation right(Consumer<Float> consumer) {
        rightHandRender = consumer;
        return this;
    }

    @Override
    public void animateItemAndHands() {
        scopeOffset.ifPresent(vec -> GlStateManager.translatef(vec.x * interpolatedValue, vec.y * interpolatedValue, vec.z * interpolatedValue));
    }

    @Override
    public void animateItem() {
        GlStateManager.translatef(itemVec.x * interpolatedValue, itemVec.y * interpolatedValue, itemVec.z * interpolatedValue);
    }

    @Override
    public void animateRightHand() {
        rightHandRender.accept(interpolatedValue);
    }

    @Override
    public void animateLeftHand() {
        leftHandRender.accept(interpolatedValue);
    }

    @Override
    public void tick() {
    }

    @Override
    public float getAnimationProgress() {
        return aimInfo.getProgress();
    }

    @Override
    public boolean isAnimationFinished() {
        return false;
    }
}
