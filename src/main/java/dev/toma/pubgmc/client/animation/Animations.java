package dev.toma.pubgmc.client.animation;

import dev.toma.pubgmc.client.animation.gun.GunAnimationPack;
import dev.toma.pubgmc.client.animation.types.RecoilAnimation;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import net.minecraft.item.ItemStack;

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
        RELOADING = register().factory(player -> {
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof AbstractGunItem) {
                AbstractGunItem item = (AbstractGunItem) stack.getItem();
                GunAnimationPack pack = item.getAnimations();
                return pack.getReloadAnimation(item, stack, true);
            }
            return null;
        });
        BOLT = register();
        RECOIL = register().factory(player -> new RecoilAnimation());
        SPRINTING = register().blockedBy(RELOADING, BOLT);
        AIMING = register().blockedBy(SPRINTING, RELOADING, BOLT);
        FIREMODE_SWITCH = register();
    }

    private static int id = -1;

    private static AnimationType register() {
        AnimationType type = new AnimationType(id);
        intToTypeMap.put(id++, type);
        return type;
    }
}
