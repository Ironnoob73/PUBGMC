package dev.toma.pubgmc.games.interfaces;

public interface IStateListener {

    boolean isChanged();

    void clear();

    void markForUpdate();

    void setState(boolean state);
}
