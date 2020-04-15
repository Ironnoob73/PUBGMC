package dev.toma.pubgmc.common.entity;

import dev.toma.pubgmc.Registry;
import dev.toma.pubgmc.api.entity.AbstractControllableEntity;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ParachuteEntity extends AbstractControllableEntity {

    public static final int TIME_TO_DEPLOY = 30;
    public static final float TURN_SPEED_LIMIT = 2.0F;

    protected float turningSpeed;
    protected int timeWithoutOwner;

    public ParachuteEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    public ParachuteEntity(World world, Entity user) {
        this(Registry.PMCEntityTypes.PARACHUTE, world);
        this.setPosition(user.posX, user.posY, user.posZ);
        this.setRotation(user.rotationYaw, user.rotationPitch);
    }

    @Override
    protected void updateMovement() {
        super.updateMovement();
        if(noRotateKey()) {
            float rotationSpeedSlowdown = 0.2f;
            turningSpeed = Math.abs(turningSpeed - rotationSpeedSlowdown) <= rotationSpeedSlowdown ? 0.0F : turningSpeed > 0 ? turningSpeed - rotationSpeedSlowdown : turningSpeed < 0 ? turningSpeed + rotationSpeedSlowdown : 0.0F;
        }
    }

    @Override
    protected void updateEntityPre() {
        if(!isBeingRidden()) {
            if(++timeWithoutOwner >= 100) {
                remove();
            }
        }
        this.rotationYaw += turningSpeed;
        if(isBeingRidden()) {
            if(onGround) {
                this.removePassengers();
                return;
            }
            Vec3d look = this.getLookVec();
            setMotion(look.x, -0.25, look.z);
        } else {
            setMotion(0, 0, 0);
        }
    }

    @Override
    protected void updateEntityPost() {
        move(MoverType.SELF, getMotion());
    }

    @Override
    protected void moveRight() {
        turningSpeed = UsefulFunctions.wrap(turningSpeed + 0.2F, -TURN_SPEED_LIMIT, TURN_SPEED_LIMIT);
    }

    @Override
    protected void moveLeft() {
        turningSpeed = UsefulFunctions.wrap(turningSpeed - 0.2F, -TURN_SPEED_LIMIT, TURN_SPEED_LIMIT);
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 256.0D;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }
}
