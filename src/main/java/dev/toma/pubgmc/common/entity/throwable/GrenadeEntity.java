package dev.toma.pubgmc.common.entity.throwable;

import dev.toma.pubgmc.init.PMCEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class GrenadeEntity extends ThrowableEntity {

    public GrenadeEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public GrenadeEntity(EntityType<?> type, World world, LivingEntity thrower, EnumEntityThrowState state) {
        super(type, world, thrower, state);
    }

    public GrenadeEntity(World world, LivingEntity thrower, EnumEntityThrowState state, int time) {
        super(PMCEntities.GRENADE, world, thrower, state, time);
    }

    @Override
    public void onExplode() {
        if(!world.isRemote) {
            this.setPosition(this.posX, this.posY + 1, this.posZ);
            world.createExplosion(null, this.posX, this.posY, this.posZ, 5.0F, Explosion.Mode.NONE);
        }
        this.remove();
    }
}
