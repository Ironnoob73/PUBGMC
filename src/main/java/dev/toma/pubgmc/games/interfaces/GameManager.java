package dev.toma.pubgmc.games.interfaces;

public interface GameManager {

    IPlayerManager getPlayerManager();

    IObjectManager getObjectManager();

    IZone getZone();
}
