package dev.toma.pubgmc.common.item.wearable;

import dev.toma.pubgmc.common.item.PMCItem;
import dev.toma.pubgmc.common.item.utility.GhillieSlotItem;
import net.minecraft.item.ItemStack;

public class GhillieSuitItem extends PMCItem implements GhillieSlotItem {

    public GhillieSuitItem(String name) {
        super(name, new Properties().maxStackSize(1).group(ITEMS));
    }

    @Override
    public int getColor(ItemStack stack) {
        return 0x6B8B42;
    }

    @Override
    public boolean blocksBackpackRender() {
        return true;
    }
}
