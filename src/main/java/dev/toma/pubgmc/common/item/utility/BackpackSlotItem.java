package dev.toma.pubgmc.common.item.utility;

public interface BackpackSlotItem {

    BackpackType getType();

    enum BackpackType {
        SMALL, MEDIUM, LARGE
    }
}
