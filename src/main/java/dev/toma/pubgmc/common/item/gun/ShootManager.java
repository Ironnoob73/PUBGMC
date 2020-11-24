package dev.toma.pubgmc.common.item.gun;

import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.common.entity.BulletEntity;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ShootManager {

    void shoot(LivingEntity source, World world, ItemStack stack);

    static void handleNormal(LivingEntity source, World world, ItemStack stack) {
        AbstractGunItem gun = (AbstractGunItem) stack.getItem();
        int inaccuracy = 15;
        if(source instanceof PlayerEntity && PlayerCapFactory.get((PlayerEntity) source).getAimInfo().isAiming()) {
            inaccuracy = 0;
        }
        world.addEntity(new BulletEntity(world, source, stack, gun.getGunDamage(), gun.getHeadshotMultiplier(), gun.getInitialVelocity(), gun.getGravityEffect(), gun.getGravityResistantTime(), inaccuracy));
    }

    static void handleShotgun(LivingEntity source, World world, ItemStack stack) {
        AbstractGunItem gun = (AbstractGunItem) stack.getItem();
        float f = gun.getAttachment(AttachmentCategory.BARREL, stack).getInaccuracyModifier();
        int inaccuracy = (int) (15 * f);
        for(int i = 0; i < 7; i++) {
            world.addEntity(new BulletEntity(world, source, stack, gun.getGunDamage(), gun.getHeadshotMultiplier(), gun.getInitialVelocity(), gun.getGravityEffect(), gun.getGravityResistantTime(), inaccuracy));
        }
    }
}
