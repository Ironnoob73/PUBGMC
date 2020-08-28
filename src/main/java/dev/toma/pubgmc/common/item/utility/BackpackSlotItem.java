package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.common.inventory.PMCInventoryItem;
import dev.toma.pubgmc.common.inventory.SlotType;

public interface BackpackSlotItem extends PMCInventoryItem {

    BackpackType getType();

    @Override
    default SlotType getSlotType() {
        return SlotType.BACKPACK;
    }

    enum BackpackType {
        SMALL, MEDIUM, LARGE
    }
}
