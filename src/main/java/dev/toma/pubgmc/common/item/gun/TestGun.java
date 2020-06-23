package dev.toma.pubgmc.common.item.gun;

import dev.toma.pubgmc.common.entity.BulletEntity;
import dev.toma.pubgmc.common.item.PMCItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class TestGun extends PMCItem {

    public TestGun() {
        super("testgun", new Properties().maxStackSize(1).group(ITEMS));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public void shoot(LivingEntity src, World world, ItemStack stack) {
        if(!world.isRemote) {
            if(!this.canShoot(src, world, stack)) return;
            world.addEntity(new BulletEntity(world, src, stack, 4.0F, 2.5F, 10.0F, 0.0F, 0, 0));
            world.playSound(null, src.posX, src.posY, src.posZ, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 1.0F, 1.0F);
        }
    }

    private boolean canShoot(LivingEntity src, World world, ItemStack stack) {
        if(src instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) src;
            CooldownTracker tracker = player.getCooldownTracker();
            if(!tracker.hasCooldown(stack.getItem())) {
                tracker.setCooldown(stack.getItem(), 1);
                return true;
            }
            return false;
        }
        return true;
    }
}
