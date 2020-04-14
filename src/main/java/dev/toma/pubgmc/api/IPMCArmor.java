package dev.toma.pubgmc.api;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IPMCArmor {

    EquipmentSlotType armorSlot();

    float damageMultiplier();

    @OnlyIn(Dist.CLIENT)
    void renderIcon(int x, int y, int x2, int y2, ItemStack stack);
}
