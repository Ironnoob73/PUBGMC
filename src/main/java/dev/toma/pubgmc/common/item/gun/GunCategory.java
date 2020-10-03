package dev.toma.pubgmc.common.item.gun;

import net.minecraft.world.World;

public enum GunCategory {

    MISC,
    PISTOL,
    SHOTGUN,
    SMG,
    AR_LMG,
    DMR,
    SR;

    public static final GunCategory[] BOT_CATEGORIES = {
            SHOTGUN, SMG, AR_LMG, DMR, SR
    };

    public static GunCategory getRandomBotCategory(World world) {
        return BOT_CATEGORIES[world.rand.nextInt(BOT_CATEGORIES.length)];
    }
}
