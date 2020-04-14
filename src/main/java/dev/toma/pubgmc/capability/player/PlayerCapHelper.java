package dev.toma.pubgmc.capability.player;

import dev.toma.pubgmc.capability.IPlayerCap;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerCapHelper {

    public static void addBoostValue(PlayerEntity playerEntity, int levels) {
        IPlayerCap cap = PlayerCapFactory.get(playerEntity);
        BoostStats stats = cap.getBoostStats();
        stats.setValue(Math.min(20, stats.getValue() + levels));
        stats.setSaturation(0.0F);
        cap.syncNetworkData();
    }
}
