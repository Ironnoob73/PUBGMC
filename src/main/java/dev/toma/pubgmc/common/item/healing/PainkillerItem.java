package dev.toma.pubgmc.common.item.healing;

import dev.toma.pubgmc.capability.player.PlayerCapHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class PainkillerItem extends HealingItem {

    public PainkillerItem(String name) {
        super(name, new Properties().group(ITEMS).maxStackSize(3));
    }

    @Override
    public String getSuccessKey() {
        return "pubgmc.heal.painkillers.success";
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 120;
    }

    @Override
    public void onFinish(PlayerEntity player) {
        PlayerCapHelper.addBoostValue(player, 12);
    }
}
