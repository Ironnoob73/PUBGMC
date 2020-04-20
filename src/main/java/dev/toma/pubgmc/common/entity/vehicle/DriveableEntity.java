package dev.toma.pubgmc.common.entity.vehicle;

import dev.toma.pubgmc.api.entity.AbstractControllableEntity;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class DriveableEntity extends AbstractControllableEntity implements IEntityAdditionalSpawnData {

    public static final float MAX_TURN_MODIFIER = 3.0F;

    public DriveableData data;
    protected float fuel;
    protected float health;
    protected float currentSpeed;
    protected float prevSpeed;
    protected float turnModifier;
    protected float prevTurnModifier;

    public DriveableEntity(EntityType<?> type, World world) {
        super(type, world);
        this.data = DriveableData.getDefault();
        this.stepHeight = 1.0F;
        this.setBaseData();
    }

    public DriveableEntity(EntityType<?> type, World world, BlockPos pos, DriveableData data) {
        super(type, world);
        this.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
        this.data = data;
        this.stepHeight = 1.0F;
        this.setBaseData();
    }

    public void burnFuel() {
        this.fuel = Math.max(0.0F, this.fuel - 0.05f);
    }

    public void setBaseData() {
        this.health = data.maxHealth;
        this.fuel = data.maxFuel;
    }

    public void refuel() {
        this.fuel = Math.min(fuel + 50.0F, data.maxFuel);
    }

    public void checkHealthState() {
        if(!world.isRemote && this.health <= 0.0F) {
            this.removePassengers();
            world.createExplosion(this, posX, posY, posZ, 5.0F, Explosion.Mode.NONE);
            this.remove();
        }
    }

    public abstract int maxUserAmount();

    protected double getPassengerX(int index) {
        return 0;
    }

    protected double getPassengerY(int index) {
        return this.getMountedYOffset();
    }

    protected double getPassengerZ(int index) {
        return 0;
    }

    public boolean canAccelerate() {
        return this.hasFuel();
    }

    @Override
    protected void moveForward() {
        if(this.canAccelerate()) {
            currentSpeed = UsefulFunctions.wrap(currentSpeed + (currentSpeed < 0 ? data.brakeSpeed : data.acceleration), -data.maxSpeed / 2, data.maxSpeed);
            this.burnFuel();
        }
    }

    @Override
    protected void moveBackward() {
        if(this.canAccelerate()) {
            currentSpeed = UsefulFunctions.wrap(currentSpeed - (currentSpeed < 0 ? data.acceleration : data.brakeSpeed), -data.maxSpeed / 2, data.maxSpeed);
            this.burnFuel();
        }
    }

    @Override
    protected void moveRight() {
        if(isMoving()) turnModifier = UsefulFunctions.wrap(currentSpeed > 0 ? turnModifier + 0.25F : turnModifier - 0.25F, -3.0F, 3.0F);
    }

    @Override
    protected void moveLeft() {
        if(isMoving()) turnModifier = UsefulFunctions.wrap(currentSpeed > 0 ? turnModifier - 0.25F : turnModifier + 0.25F, -3.0F, 3.0F);
    }

    @Override
    protected void updateMovement() {
        super.updateMovement();
        if(noVerticalKey() || !hasFuel()) {
            float amount = data.acceleration / 3;
            currentSpeed = Math.abs(currentSpeed - amount) <= amount ? 0.0F : currentSpeed > 0 ? currentSpeed - amount : currentSpeed < 0 ? currentSpeed + amount : 0.0F;
        }
        if(noRotateKey()) {
            if(noRotateKey()) {
                float rotationSpeedSlowdown = 1f;
                turnModifier = Math.abs(turnModifier - rotationSpeedSlowdown) <= rotationSpeedSlowdown ? 0.0F : turnModifier > 0 ? turnModifier - rotationSpeedSlowdown : turnModifier < 0 ? turnModifier + rotationSpeedSlowdown : 0.0F;
            }
        }
        if(onGround) this.rotationYaw += turnModifier;
        Vec3d lookVec = this.getLookVec();
        this.setMotion(lookVec.mul(currentSpeed, currentSpeed, currentSpeed));
    }

    @Override
    protected void updateEntityPost() {
        this.setMotion(this.getMotion().x, -0.25, this.getMotion().z);
        move(MoverType.SELF, this.getMotion());
        this.checkHealthState();
        if(!this.isBeingRidden()) {
            resetInputState();
        }
    }

    @Override
    public boolean processInitialInteract(PlayerEntity player, Hand hand) {
        if(!world.isRemote) {
            if(!isPassenger(player) && canFitPassenger(player)) {
                player.startRiding(this);
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < this.maxUserAmount();
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if(this.isPassenger(passenger)) {
            int id = this.getPassengers().indexOf(passenger);
            double x = this.getPassengerX(id);
            double y = this.getPassengerY(id);
            double z = this.getPassengerZ(id);
            Vec3d vec = (new Vec3d(x, y, z)).rotateYaw(-this.rotationYaw * 0.017453292F - ((float) Math.PI / 2f));
            passenger.setPosition(posX + vec.x, posY + vec.y, posZ + vec.z);
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.put("data", data.write());
        compound.putFloat("fuel", fuel);
        compound.putFloat("health", health);
        compound.putFloat("speed", currentSpeed);
        compound.putFloat("turn", turnModifier);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        data = DriveableData.getDefault();
        data.read(compound.contains("data") ? compound.getCompound("data") : new CompoundNBT());
        fuel = compound.getFloat("fuel");
        health = compound.getFloat("health");
        currentSpeed = compound.getFloat("speed");
        turnModifier = compound.getFloat("turn");
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        CompoundNBT c = new CompoundNBT();
        this.writeAdditional(c);
        buffer.writeCompoundTag(c);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(!this.isPassenger(source.getTrueSource())) {
            this.health -= amount;
            return true;
        }
        return false;
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.readAdditional(additionalData.readCompoundTag());
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public float getTurnModifier() {
        return turnModifier;
    }

    @Override
    protected void registerData() {
    }

    public boolean hasFuel() {
        return fuel > 0;
    }

    public boolean hasHealth() {
        return health > 0;
    }

    public boolean isMoving() {
        return currentSpeed != 0;
    }

    public float getInterpolatedSpeed(float partialTicks) {
        return MathHelper.lerp(partialTicks, prevSpeed, currentSpeed);
    }

    public float getInterpolatedTurning(float partialTicks) {
        return MathHelper.lerp(partialTicks, prevTurnModifier, turnModifier);
    }

    public static class DriveableData {
        private float maxFuel;
        private float maxHealth;
        private float maxSpeed;
        private float acceleration;
        private float brakeSpeed;

        public static DriveableData getDefault() {
            return new DriveableData(100F, 100.0F, 1.0F, 0.2F, 0.4F);
        }

        public DriveableData(float maxFuel, float maxHealth, float maxSpeed, float acceleration, float brakeSpeed) {
            this.maxFuel = maxFuel;
            this.maxHealth = maxHealth;
            this.maxSpeed = maxSpeed;
            this.acceleration = acceleration;
            this.brakeSpeed = brakeSpeed;
        }

        public float getMaxFuel() {
            return maxFuel;
        }

        public float getMaxHealth() {
            return maxHealth;
        }

        public float getMaxSpeed() {
            return maxSpeed;
        }

        public float getAcceleration() {
            return acceleration;
        }

        public float getBrakeSpeed() {
            return brakeSpeed;
        }

        public CompoundNBT write() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putFloat("fuel", maxFuel);
            nbt.putFloat("health", maxHealth);
            nbt.putFloat("speed", maxSpeed);
            nbt.putFloat("acc", acceleration);
            nbt.putFloat("brk", brakeSpeed);
            return nbt;
        }

        public void read(CompoundNBT nbt) {
            maxFuel = nbt.getFloat("fuel");
            maxHealth = nbt.getFloat("health");
            maxSpeed = nbt.getFloat("speed");
            acceleration = nbt.getFloat("acc");
            brakeSpeed = nbt.getFloat("brk");
        }
    }
}
