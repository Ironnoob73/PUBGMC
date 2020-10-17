package dev.toma.pubgmc.games.interfaces;

import net.minecraft.entity.LivingEntity;

public interface IGameManager {

    IPlayerManager getPlayerManager();

    IObjectManager getObjectManager();

    IZone getZone();

    boolean createDeathCrate(LivingEntity entity);
}
