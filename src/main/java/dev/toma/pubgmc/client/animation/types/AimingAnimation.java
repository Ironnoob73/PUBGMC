package dev.toma.pubgmc.client.animation.types;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.capability.player.AimInfo;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.client.animation.AnimationFactory;
import dev.toma.pubgmc.util.object.Optional;
import net.minecraft.client.Minecraft;

import javax.vecmath.Vector3f;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AimingAnimation extends AnimationFactory {

    private final AimInfo aimInfo;
    private final Optional<Vector3f> scopeOffset = Optional.empty();
    private final Vector3f itemVec;
    private Consumer<Float> leftHandRender = f -> {};
    private Consumer<Float> rightHandRender = f -> {};

    public static Supplier<AimingAnimation> defaultPistol() {
        return () -> new AimingAnimation(-0.56F, 0.1F, 0.0F).right(f -> {
            GlStateManager.translatef(-0.4F * f, 0.125F * f, 0.1F * f);
            GlStateManager.rotatef(10.0F * f, 0.0F, 1.0F, 0.0F);
        }).left(f -> {
            GlStateManager.translatef(-0.35F * f, 0.125F * f, 0.2F * f);
            GlStateManager.rotatef(15.0F * f, 0.0F, 1.0F, 0.0F);
        });
    }

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
