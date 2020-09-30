package dev.toma.pubgmc.games;

import dev.toma.pubgmc.games.util.GameStorage;

public class ShrinkableZone extends StaticZone {

    private int shrinkIndex;


    public ShrinkableZone(GameStorage storage) {
        super(storage);
    }

    public void pregenZoneStages() {

    }

    @Override
    public void startShrinking(int ticks) {
        super.startShrinking(ticks);
    }

    @Override
    public boolean isShrinking() {
        return super.isShrinking();
    }
}
