package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.common.inventory.PMCInventoryItem;
import dev.toma.pubgmc.common.inventory.SlotType;
import net.minecraft.item.ItemStack;

public interface GhillieSlotItem extends PMCInventoryItem {

    int getColor(ItemStack stack);

    boolean blocksBackpackRender();

    @Override
    default SlotType getSlotType() {
        return SlotType.GHILLIE;
    }
}
