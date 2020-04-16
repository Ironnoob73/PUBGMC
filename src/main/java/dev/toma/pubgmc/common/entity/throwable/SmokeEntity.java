package dev.toma.pubgmc.common.entity.throwable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

public class SmokeEntity extends ThrowableEntity {

    public static final int SMOKE_EFFECT_DURATION = 400;
    public static final int SMOKE_ANGLE_PER_TICK = 90;

    private boolean startedSmoking;
    private int effectLeft = SMOKE_EFFECT_DURATION;
    private int currentAngle;

    private double smokeHeight = 0;

    public SmokeEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public SmokeEntity(EntityType<?> type, World world, LivingEntity thrower, EnumEntityThrowState state) {
        super(type, world, thrower, state);
    }

    public SmokeEntity(EntityType<?> type, World world, LivingEntity thrower, EnumEntityThrowState state, int timeLeft) {
        super(type, world, thrower, state, timeLeft);
    }

    @Override
    public void onExplode() {
        this.startedSmoking = true;
    }

    public void createSmokeParticles() {
        if(this.world.getBlockState(this.getPosition()).getMaterial().isLiquid()) {
            world.addParticle(ParticleTypes.BUBBLE, this.posX, this.posY, this.posZ, 0, 0.2, 0);
            return;
        }
        this.currentAngle += SMOKE_ANGLE_PER_TICK;
        if(world.isRemote) {
            for(int i = SMOKE_ANGLE_PER_TICK; i > 0; i -= 5) {
                int updatedAngle = (this.currentAngle - i) % 360;
                double angle = Math.toRadians(updatedAngle);
                double x = Math.sin(angle) / 3;
                double z = Math.cos(angle) / 3;
                double y = 0;
                double d = 0;
                for(; d < this.smokeHeight; d += 0.5D) {
                    this.world.addParticle(ParticleTypes.CLOUD, true, this.posX, this.posY + d, this.posZ, x, y, z);
                }
            }
        }
    }

    @Override
    public void onThrowableTick() {
        if(startedSmoking) {
            if(this.smokeHeight < 3.5D) {
                if(ticksExisted % 5 == 0) {
                    this.smokeHeight += 0.25D;
                }
            }
            this.createSmokeParticles();
            --this.effectLeft;
            if(!world.isRemote && effectLeft <= 0) {
                this.remove();
            }
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("smoking", this.startedSmoking);
        compound.putInt("left", this.effectLeft);
        compound.putInt("angle", this.currentAngle);
        compound.putDouble("smokeHeight", this.smokeHeight);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.startedSmoking = compound.getBoolean("smoking");
        this.effectLeft = compound.getInt("left");
        this.currentAngle = compound.getInt("angle");
        this.smokeHeight = compound.getDouble("smokeHeight");
    }
}
