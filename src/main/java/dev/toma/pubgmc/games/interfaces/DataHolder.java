package dev.toma.pubgmc.games.interfaces;

public interface DataHolder<H extends IDataHandler<?, ?>> {

    H getData();

    default void resetData() {
        getData().getStoredData().clear();
    }
}
