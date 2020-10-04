package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.common.inventory.PMCInventoryItem;
import dev.toma.pubgmc.common.inventory.SlotType;
import net.minecraft.util.ResourceLocation;

public interface BackpackSlotItem extends PMCInventoryItem, Comparable<BackpackSlotItem> {

    BackpackType getType();

    ResourceLocation getBackpackTexture();

    @Override
    default SlotType getSlotType() {
        return SlotType.BACKPACK;
    }

    @Override
    default int compareTo(BackpackSlotItem o) {
        return o.getType().ordinal() - getType().ordinal();
    }

    enum BackpackType {
        SMALL, MEDIUM, LARGE
    }
}
