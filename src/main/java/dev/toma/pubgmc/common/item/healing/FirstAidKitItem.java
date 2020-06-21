package dev.toma.pubgmc.common.item.healing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class FirstAidKitItem extends HealingItem {

    public FirstAidKitItem(String name) {
        super(name, new Properties().group(ITEMS).maxStackSize(1));
    }

    @Override
    public String getSuccessKey() {
        return "pubgmc.heal.first_aid.success";
    }

    @Override
    public String getFailKey() {
        return "pubgmc.heal.first_aid.fail";
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 120;
    }

    @Override
    public void onFinish(PlayerEntity player) {
        player.setHealth(15.0F);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getHealth() < 15;
    }
}
