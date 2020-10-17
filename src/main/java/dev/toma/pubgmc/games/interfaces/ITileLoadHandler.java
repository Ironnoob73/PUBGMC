package dev.toma.pubgmc.games.interfaces;

public interface ITileLoadHandler extends IKeyHolder {

    /**
     * Called when new chunk is loaded in world while
     * some Game instance is running
     */
    void load(IKeyHolder key);
}
