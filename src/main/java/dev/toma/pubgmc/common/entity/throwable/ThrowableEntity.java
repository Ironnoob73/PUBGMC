package dev.toma.pubgmc.common.entity.throwable;

import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketSyncEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class ThrowableEntity extends Entity implements IEntityAdditionalSpawnData {

    public static final float AIR_DRAG_MODIFIER = 0.98F;
    public static final float GROUND_DRAG_MODIFIER = 0.7F;
    public static final float BOUNCE_MODIFIER = 0.8F;

    public int fuse;
    public float rotation;
    public float lastRotation;
    public int timesBounced = 0;
    public boolean isFrozen = false;

    public ThrowableEntity(EntityType<?> type, World world) {
        this(type, world, null, EnumEntityThrowState.FORCED);
    }

    public ThrowableEntity(EntityType<?> type, World world, LivingEntity thrower, EnumEntityThrowState state) {
        this(type, world, thrower, state, state == EnumEntityThrowState.FORCED ? 1 : 100);
    }

    public ThrowableEntity(EntityType<?> type, World world, LivingEntity thrower, EnumEntityThrowState state, int time) {
        super(type, world);
        if(thrower != null) this.setPosition(thrower.posX, thrower.posY + thrower.getEyeHeight(), thrower.posZ);
        this.fuse = time;

        this.setInitialMotion(state, thrower);
    }

    public abstract void onExplode();

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        --this.fuse;
        if(fuse < 0) {
            this.onExplode();
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        Vec3d m = this.getMotion();
        double prevMotionX = m.x;
        double prevMotionY = m.y;
        double prevMotionZ = m.z;
        if(!world.isRemote) {
            Vec3d from = this.getPositionVec();
            Vec3d to = from.add(m);
            BlockRayTraceResult rayTraceResult = this.world.rayTraceBlocks(new RayTraceContext(from, to, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, this));
            if(rayTraceResult != null && rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                this.onCollide(from, to, rayTraceResult);
            }
        }
        this.move(MoverType.SELF, m);
        if(getMotion().x != prevMotionX) {
            setMotion(-BOUNCE_MODIFIER * prevMotionX, getMotion().y, getMotion().z);
            this.onGrenadeBounce(BounceAxis.X);
        }
        if(getMotion().y != prevMotionY) {
            setMotion(getMotion().x, -BOUNCE_MODIFIER * prevMotionY, getMotion().z);
            this.onGrenadeBounce(BounceAxis.Y);
        }
        if(getMotion().z != prevMotionZ) {
            setMotion(getMotion().x, getMotion().y, -BOUNCE_MODIFIER * prevMotionZ);
            this.onGrenadeBounce(BounceAxis.Z);
        }
        if(!this.hasNoGravity()) {
            setMotion(getMotion().x, getMotion().y - 0.039D, getMotion().z);
        }
        m = this.getMotion();
        setMotion(m.x * AIR_DRAG_MODIFIER, m.y * AIR_DRAG_MODIFIER, m.z * AIR_DRAG_MODIFIER);
        if(this.onGround) {
            m = this.getMotion();
            setMotion(m.x * GROUND_DRAG_MODIFIER, m.y * GROUND_DRAG_MODIFIER, m.z * GROUND_DRAG_MODIFIER);
        }
        this.lastRotation = this.rotation;
        if(world.isRemote && !this.onGround) {
            if(this.getMotion().x != 0 && this.getMotion().y != 0 && this.getMotion().z != 0) {
                this.rotation += 45F;
            }
        }

        this.onThrowableTick();
    }

    public final void onCollide(Vec3d from, Vec3d to, BlockRayTraceResult result) {
        BlockPos pos = result.getPos();
        BlockState state = this.world.getBlockState(pos);
        boolean flag = this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING);
        if(flag) {
            boolean hasBrokenGlass = false;
            // TODO
            /*if(state.getBlock() instanceof BlockWindow) {
                BlockWindow window = (BlockWindow) state.getBlock();
                boolean isBroken = state.getValue(BlockWindow.BROKEN);
                if(!isBroken) {
                    window.breakWindow(state, pos, this.world);
                    hasBrokenGlass = true;
                }
            } else*/ if(state.getMaterial() == Material.GLASS) {
                world.destroyBlock(pos, false);
                hasBrokenGlass = true;
            }
            if(hasBrokenGlass) {
                RayTraceContext ctx = new RayTraceContext(from, to, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, this);
                BlockRayTraceResult rayTraceResult = this.world.rayTraceBlocks(ctx);
                if(rayTraceResult != null && rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
                    this.onCollide(from, to, rayTraceResult);
                }
            }
        }
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    public void onThrowableTick() {

    }

    public void bounce() {

    }

    public boolean canBounce() {
        return true;
    }

    public final boolean isFirstBounce() {
        return timesBounced == 1;
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("fuse", this.fuse);
        compound.putBoolean("frozen", this.isFrozen);
        compound.putInt("timesBounced", this.timesBounced);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.fuse = compound.getInt("fuse");
        this.isFrozen = compound.getBoolean("frozen");
        this.timesBounced = compound.getInt("timesBounced");
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(fuse);
        buffer.writeBoolean(isFrozen);
        buffer.writeInt(timesBounced);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        fuse = additionalData.readInt();
        isFrozen = additionalData.readBoolean();
        timesBounced = additionalData.readInt();
    }

    protected void onEntityFrozen() {
        NetworkManager.sendToAll(world, new CPacketSyncEntity(this.getEntityId(), this.writeWithoutTypeId(new CompoundNBT())));
    }

    private void setInitialMotion(EnumEntityThrowState state, LivingEntity thrower) {
        if (thrower == null) {
            return;
        }
        float sprintModifier = 1.25F;
        float modifier = state.getModifier();
        if(thrower.isSprinting()) modifier *= sprintModifier;
        Vec3d viewVec = thrower.getLookVec();
        this.setMotion(viewVec.x * modifier, viewVec.y * modifier / sprintModifier, viewVec.z * modifier);
    }

    private void freezeEntity() {
        this.setMotion(0, 0, 0);
        // to make sure it won't get accidentally called twice
        if(isFrozen) return;
        this.isFrozen = true;
        this.onEntityFrozen();
    }

    private void onGrenadeBounce(BounceAxis axis) {
        Vec3d mot = this.getMotion();
        if(Math.sqrt(mot.x*mot.x+mot.y*mot.y+mot.z*mot.z) >= 0.2) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.BLOCK_ANVIL_BREAK, SoundCategory.MASTER, 1.0F, 1.8F);
        }
        this.timesBounced++;
        if(!canBounce()) {
            this.freezeEntity();
        }
        this.bounce();
    }

    public enum EnumEntityThrowState {
        LONG(1.4F),
        SHORT(0.6F),
        FORCED(0.0F);

        private final float modifier;

        EnumEntityThrowState(float modifier) {
            this.modifier = modifier;
        }

        public float getModifier() {
            return modifier;
        }
    }

    public enum BounceAxis {
        X, Y, Z
    }
}
