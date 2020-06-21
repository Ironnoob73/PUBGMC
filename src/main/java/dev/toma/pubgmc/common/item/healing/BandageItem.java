package dev.toma.pubgmc.common.item.healing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class BandageItem extends HealingItem {

    public BandageItem(String name) {
        super(name, new Properties().group(ITEMS).maxStackSize(5));
    }

    @Override
    public String getSuccessKey() {
        return "pubgmc.heal.bandage.success";
    }

    @Override
    public String getFailKey() {
        return "pubgmc.heal.bandage.fail";
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 40;
    }

    @Override
    public void onFinish(PlayerEntity player) {
        player.setHealth(Math.min(15, player.getHealth() + 3));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getHealth() < 15;
    }
}
