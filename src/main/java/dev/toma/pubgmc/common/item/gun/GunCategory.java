package dev.toma.pubgmc.common.item.gun;

import net.minecraft.world.World;

public enum GunCategory {

    MISC(25),
    PISTOL(30),
    SHOTGUN(15),
    SMG(40),
    AR_LMG(70),
    DMR(100),
    SR(130);

    final double range;

    GunCategory(double range) {
        this.range = range;
    }

    public double getRange() {
        return range;
    }

    public static final GunCategory[] BOT_CATEGORIES = {
            SHOTGUN, SMG, AR_LMG, DMR, SR
    };

    public static GunCategory getRandomBotCategory(World world) {
        return BOT_CATEGORIES[world.rand.nextInt(BOT_CATEGORIES.length)];
    }
}
