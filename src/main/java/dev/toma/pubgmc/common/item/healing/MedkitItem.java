package dev.toma.pubgmc.common.item.healing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class MedkitItem extends HealingItem {

    public MedkitItem(String name) {
        super(name, new Properties().group(ITEMS).maxStackSize(1));
    }

    @Override
    public String getSuccessKey() {
        return "pubgmc.heal.medkit.success";
    }

    @Override
    public String getFailKey() {
        return "pubgmc.heal.medkit.fail";
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 160;
    }

    @Override
    public void onFinish(PlayerEntity player) {
        player.setHealth(20.0F);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getHealth() < 20;
    }
}
