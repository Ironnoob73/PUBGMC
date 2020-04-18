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

    protected float turningSpeed;
    public int timeWithoutOwner;

    public ParachuteEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    public ParachuteEntity(World world, Entity user) {
        this(Registry.PMCEntityTypes.PARACHUTE, world);
        this.setPosition(user.posX, user.posY, user.posZ);
        this.setRotation(user.rotationYaw, 0.0F);
        this.setMotion(user.getMotion());
    }

    @Override
    public double getMountedYOffset() {
        return 0.5d;
    }

    @Override
    protected void updateMovement() {
        super.updateMovement();
        if(noRotateKey()) {
            float rotationSpeedSlowdown = 1f;
            turningSpeed = Math.abs(turningSpeed - rotationSpeedSlowdown) <= rotationSpeedSlowdown ? 0.0F : turningSpeed > 0 ? turningSpeed - rotationSpeedSlowdown : turningSpeed < 0 ? turningSpeed + rotationSpeedSlowdown : 0.0F;
        }
        if(noVerticalKey()) {
            rotationPitch = Math.abs(rotationPitch - 1.0F) <= 1.0F ? 0.0F : rotationPitch > 0 ? rotationPitch - 1.0F : rotationPitch < 0 ? rotationPitch + 1.0F : 0.0F;
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
            if(onGround || collided) {
                this.removePassengers();
                return;
            }
            if(!isDeployed()) return;
            this.fallDistance = 0.0F;
            Vec3d look = this.getLookVec();
            double x = look.x / 2;
            double z = look.z / 2;
            double speedModifier =  1 + rotationPitch / 30.0F;
            setMotion(x, -0.25 * speedModifier, z);
        } else {
            setMotion(0, 0, 0);
        }
    }

    protected boolean isDeployed() {
        return ticksExisted > 20;
    }

    @Override
    protected void updateEntityPost() {
        move(MoverType.SELF, getMotion());
        if(!this.isBeingRidden()) {
            resetInputState();
        }
    }

    @Override
    protected void moveRight() {
        if(isDeployed()) turningSpeed = UsefulFunctions.wrap(turningSpeed + 1F, -5F, 5F);
    }

    @Override
    protected void moveLeft() {
        if(isDeployed()) turningSpeed = UsefulFunctions.wrap(turningSpeed - 1F, -5F, +5F);
    }

    @Override
    protected void moveForward() {
        if(isDeployed()) rotationPitch = UsefulFunctions.wrap(rotationPitch + 1.5F, -15.0F, 30.0F);
    }

    @Override
    protected void moveBackward() {
        if(isDeployed())  rotationPitch = UsefulFunctions.wrap(rotationPitch - 1F, -15.0F, 30.0F);
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
