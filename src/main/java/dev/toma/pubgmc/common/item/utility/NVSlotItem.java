package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.common.inventory.PMCInventoryItem;
import dev.toma.pubgmc.common.inventory.SlotType;

public interface NVSlotItem extends PMCInventoryItem {

    @Override
    default SlotType getSlotType() {
        return SlotType.NIGHT_VISION;
    }
}
