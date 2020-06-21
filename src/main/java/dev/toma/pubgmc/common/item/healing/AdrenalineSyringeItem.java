package dev.toma.pubgmc.common.item.healing;

import dev.toma.pubgmc.capability.player.PlayerCapHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class AdrenalineSyringeItem extends HealingItem {

    public AdrenalineSyringeItem(String name) {
        super(name, new Properties().group(ITEMS).maxStackSize(1));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 120;
    }

    @Override
    public String getSuccessKey() {
        return "pubgmc.heal.adrenaline_syringe.success";
    }

    @Override
    public void onFinish(PlayerEntity player) {
        PlayerCapHelper.addBoostValue(player, 20);
    }
}
