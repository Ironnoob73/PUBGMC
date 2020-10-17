package dev.toma.pubgmc.common.inventory;

import dev.toma.pubgmc.common.entity.EquipmentHolder;
import net.minecraftforge.items.IItemHandler;

public interface IHasInventory {

    EquipmentHolder getInventory();

    void transferTo(IItemHandler handler);
}
