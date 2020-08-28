package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.common.inventory.PMCInventoryItem;
import dev.toma.pubgmc.common.inventory.SlotType;

public interface GhillieSlotItem extends PMCInventoryItem {

    @Override
    default SlotType getSlotType() {
        return SlotType.GHILLIE;
    }
}
