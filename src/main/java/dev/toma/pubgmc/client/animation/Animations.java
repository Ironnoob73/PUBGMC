package dev.toma.pubgmc.client.animation;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.builder.AnimationType;
import dev.toma.pubgmc.util.object.Pair;

import java.util.concurrent.Callable;

public class Animations {

    public static AnimationType DEBUG;
    public static AnimationType HEALING;
    public static AnimationType AIMING;
    public static AnimationType SPRINTING;
    public static AnimationType RELOADING;
    public static AnimationType BOLT;
    public static AnimationType RECOIL;

    public static void init() {
        DEBUG = register(-1);
        HEALING = register(0);
        AIMING = register(1).blockedBy(() -> new AnimationType[] {SPRINTING, RELOADING, BOLT});
        SPRINTING = register(2).blockedBy(() -> new AnimationType[] {RELOADING, BOLT});
        RELOADING = register(3);
        BOLT = register(4);
        RECOIL = register(5);
    }

    private static AnimationType register(int id) {
        return new AnimationType(id);
    }

    public static Callable<Pair<Runnable, Runnable>> standartPistolHeldAnimation() {
        return () -> Pair.of(() -> {
            GlStateManager.translatef(0.45F, -0.2F, 0.1F);
            GlStateManager.rotatef(-30.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(10.0F, 1.0F, 0.0F, 0.0F);
        }, () -> {
            GlStateManager.translatef(0.35F, -0.15F, 0.3F);
            GlStateManager.rotatef(10.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(10.0F, 1.0F, 0.0F, 0.0F);
        });
    }
}
