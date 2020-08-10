package dev.toma.pubgmc.client.animation;

import dev.toma.pubgmc.client.animation.builder.AnimationType;
import dev.toma.pubgmc.client.animation.types.RecoilAnimation;

import java.util.HashMap;
import java.util.Map;

public class Animations {

    private static final Map<Integer, AnimationType> intToTypeMap = new HashMap<>();
    public static AnimationType DEBUG;
    public static AnimationType HEALING;
    public static AnimationType AIMING;
    public static AnimationType SPRINTING;
    public static AnimationType RELOADING;
    public static AnimationType BOLT;
    public static AnimationType RECOIL;
    public static AnimationType FIREMODE_SWITCH;

    public static AnimationType getByID(int id) {
        return intToTypeMap.get(id);
    }

    public static void init() {
        DEBUG = register();
        HEALING = register();
        AIMING = register().blockedBy(() -> new AnimationType[] {SPRINTING, RELOADING, BOLT});
        SPRINTING = register().blockedBy(() -> new AnimationType[] {RELOADING, BOLT});
        RELOADING = register();
        BOLT = register();
        RECOIL = register().factory(RecoilAnimation::new);
        FIREMODE_SWITCH = register();
    }

    private static int id = -1;

    private static AnimationType register() {
        AnimationType type = new AnimationType(id);
        intToTypeMap.put(id++, type);
        return type;
    }
}
