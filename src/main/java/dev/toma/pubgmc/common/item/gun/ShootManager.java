package dev.toma.pubgmc.common.item.gun;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ShootManager {

    void shoot(LivingEntity source, World world, ItemStack stack);
}
