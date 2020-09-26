package dev.toma.pubgmc.common.entity.vehicle;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.ClientManager;
import dev.toma.pubgmc.util.RenderHelper;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.block.BlockState;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.EntityTickableSound;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

public abstract class DriveableEntity extends AbstractControllableEntity implements IEntityAdditionalSpawnData {

    public static final float MAX_TURN_MODIFIER = 3.0F;

    public DriveableData data;
    protected float fuel;
    protected float health;
    protected float currentSpeed;
    protected float lastSpeed;
    protected float turnModifier;
    protected VehicleState currentState = VehicleState.IDLE, lastState;
    @OnlyIn(Dist.CLIENT)
    public EntityTickableSound sound;

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
        this.fuel = Math.max(0.0F, this.fuel - 0.01f);
    }

    public void setBaseData() {
        this.health = data.maxHealth;
        this.fuel = data.maxFuel;
    }

    public void refuel() {
        this.fuel = Math.min(fuel + 50.0F, data.maxFuel);
    }

    public void checkHealthState() {
        this.currentState = VehicleState.getState(this);
        if(currentState != lastState) {
            if(world.isRemote) {
                ClientManager.playVehicleSound(this);
            }
        }
        if(!world.isRemote && this.health <= 0.0F) {
            this.removePassengers();
            world.createExplosion(this, posX, posY, posZ, 5.0F, Explosion.Mode.NONE);
            this.remove();
        }
    }

    public abstract int maxUserAmount();

    public abstract VehicleSoundStorage getSoundStorage();

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
    protected void updateEntityPre() {
        Vec3d at = this.getPositionVec();
        Vec3d next = at.add(this.getMotion());
        Entity entity = collidedWith(at, next, getBoundingBox().expand(getMotion()).grow(1.0D));
        if(entity != null) {
            float damage = currentSpeed < 0.15F ? 0.0F : currentSpeed * 20.0F;
            if(damage > 0)
                entity.attackEntityFrom(VEHICLE_DAMAGE, damage);
            entity.setMotion(getMotion().inverse().add(0, currentSpeed, 0));
        }
    }

    @Override
    protected void updateEntityPost() {
        this.setMotion(this.getMotion().x, -0.25, this.getMotion().z);
        move(MoverType.SELF, this.getMotion());
        this.checkHealthState();
        if(!this.isBeingRidden()) {
            resetInputState();
        }
        if(this.collidedHorizontally) {
            float damage = currentSpeed < 0.3F ? 0.0F : currentSpeed * 20.0F;
            this.attackEntityFrom(DamageSource.FALL, damage);
            for(Entity entity : this.getPassengers()) {
                if(!entity.isInvulnerable()) {
                    entity.attackEntityFrom(VEHICLE_DAMAGE, damage / 2.0F);
                }
            }
            this.currentSpeed = 0.0F;
            this.setMotion(0, 0, 0);
        }
        this.handleParticles();
        this.lastSpeed = this.currentSpeed;
        this.lastState = this.currentState;
    }

    public void handleParticles() {
        if(world.isRemote) {
            float healthPct = health / data.maxHealth;
            if(healthPct <= 0.35f) {
                Optional<Vec3d> engine = Optional.of(this.getEngineVector());
                engine.ifPresent(vec -> {
                    Vec3d vec3d = vec.rotateYaw(-rotationYaw * 0.0175F - ((float) Math.PI / 2.0F));
                    world.addParticle(ParticleTypes.LARGE_SMOKE, true, posX + vec3d.x, posY + vec3d.y, posZ + vec3d.z, 0, 0.075d, 0);
                    if(healthPct <= 0.15f) {
                        double offsetX = (rand.nextDouble() - rand.nextDouble()) * 0.1D;
                        double offsetZ = (rand.nextDouble() - rand.nextDouble()) * 0.1D;
                        world.addParticle(ParticleTypes.FLAME, true, posX + vec3d.x, posY + vec3d.y, posZ + vec3d.z, offsetX, 0.1D, offsetZ);
                        world.addParticle(ParticleTypes.LARGE_SMOKE, true, posX + vec3d.x, posY + vec3d.y, posZ + vec3d.z, -offsetX, 0.075d, -offsetZ);
                    }
                });
            }
        }
    }

    public Vec3d getEngineVector() {
        return null;
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
    public void applyOrientationToEntity(Entity entity) {
        float f = MathHelper.wrapDegrees(entity.rotationYaw - this.rotationYaw);
        float f1 = MathHelper.clamp(f, -120.0F, 120.0F);
        entity.prevRotationYaw += f1 - f;
        entity.rotationYaw += f1 - f;
        entity.setRotationYawHead(entity.rotationYaw);
    }

    @Override
    protected final void playStepSound(BlockPos pos, BlockState blockIn) {

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

    public VehicleState getCurrentState() {
        return currentState;
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
        compound.putFloat("lastSpeed", lastSpeed);
        compound.putFloat("turn", turnModifier);
        compound.putInt("state", currentState.ordinal());
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        data = DriveableData.getDefault();
        data.read(compound.contains("data") ? compound.getCompound("data") : new CompoundNBT());
        fuel = compound.getFloat("fuel");
        health = compound.getFloat("health");
        currentSpeed = compound.getFloat("speed");
        lastSpeed = compound.getFloat("lastSpeed");
        turnModifier = compound.getFloat("turn");
        currentState = VehicleState.values()[compound.getInt("state")];
        lastState = VehicleState.IDLE;
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
            this.health = Math.max(0.0F, this.health - amount);
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawOnScreen(Minecraft mc, MainWindow window) {
        int left = 15;
        int top = window.getScaledHeight() - 30;
        int width = window.getScaledWidth() / 5;
        Vec3d motion = this.getMotion();
        double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z) * 20;
        String speedString =  (int)(speed * 3.6d) + " km/h";
        mc.fontRenderer.drawStringWithShadow(speedString, left, top - 8, 0xFFFFFF);
        mc.fontRenderer.drawStringWithShadow("E", left - 7, top + 1, 0xBB0000);
        mc.fontRenderer.drawStringWithShadow("F", left + width + 2, top + 1, 0x00BB00);
        RenderHelper.drawColoredShape(left, top, left + width, top + 10, 0.4F, 0.4F, 0.4F, 0.5F);
        float fuel = this.fuel / this.data.getMaxFuel();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.disableTexture();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(left, top + 10, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        builder.pos(left + width * fuel, top + 10, 0).color(1.0F - fuel, fuel, 0.0F, 1.0F).endVertex();
        builder.pos(left + width * fuel, top, 0).color(1.0F - fuel, fuel, 0.0F, 1.0F).endVertex();
        builder.pos(left, top, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture();
        RenderHelper.drawColoredShape(left, top + 10, left + width, top + 20, 0.0F, 0.0F, 0.0F, 0.5F);
        float health = this.health / this.data.getMaxHealth();
        RenderHelper.drawColoredShape(left, top + 10, (int)(left + width * health), top + 20, 1.0F, health, health, 1.0F);
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

    public float getCurrentSpeed() {
        return currentSpeed;
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
