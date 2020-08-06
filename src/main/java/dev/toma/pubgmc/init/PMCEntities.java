package dev.toma.pubgmc.init;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.entity.BulletEntity;
import dev.toma.pubgmc.common.entity.ParachuteEntity;
import dev.toma.pubgmc.common.entity.throwable.FlashEntity;
import dev.toma.pubgmc.common.entity.throwable.GrenadeEntity;
import dev.toma.pubgmc.common.entity.vehicle.AirDriveableEntity;
import dev.toma.pubgmc.common.entity.vehicle.LandDriveableEntity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Pubgmc.MODID)
public class PMCEntities {
    public static final EntityType<ParachuteEntity> PARACHUTE = null;
    public static final EntityType<GrenadeEntity> GRENADE = null;
    public static final EntityType<GrenadeEntity> SMOKE = null;
    public static final EntityType<GrenadeEntity> MOLOTOV = null;
    public static final EntityType<FlashEntity> FLASH = null;
    public static final EntityType<LandDriveableEntity.UAZDriveable> UAZ = null;
    public static final EntityType<AirDriveableEntity.GliderDriveable> GLIDER = null;
    public static final EntityType<BulletEntity> BULLET = null;
}
