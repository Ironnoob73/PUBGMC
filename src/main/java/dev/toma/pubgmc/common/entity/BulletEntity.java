package dev.toma.pubgmc.common.entity;

import dev.toma.pubgmc.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BulletEntity extends Entity implements IEntityAdditionalSpawnData {

    private float damage;
    private float headshotMultiplier;
    private float gravity;
    private int ticks;

    private LivingEntity source;
    private ItemStack stack;

    public BulletEntity(EntityType<?> type, World world) {
        super(type, world);
        noClip = true;
    }

    public BulletEntity(World world, @Nonnull LivingEntity src, @Nonnull ItemStack stack, float damage, float headshotMultiplier, float velocity, float gravity, int ticks, int inaccuracy) {
        this(Registry.PMCEntityTypes.BULLET, world);
        setPosition(src.posX, src.posY + src.getEyeHeight(), src.posZ);
        // TODO inaccuracy
        Vec3d vec3d = getVectorForRotation(src.rotationPitch, src.rotationYaw);
        double x = vec3d.x * velocity;
        double y = vec3d.y * velocity;
        double z = vec3d.z * velocity;
        setMotion(x, y, z);
        this.damage = damage;
        this.headshotMultiplier = headshotMultiplier;
        this.gravity = gravity;
        this.ticks = ticks;
        this.source = src;
        this.stack = stack;
        updateDirection();
    }

    public void updateDirection() {
        Vec3d motion = this.getMotion();
        float f = MathHelper.sqrt(motion.x * motion.x + motion.z * motion.z);
        rotationYaw = (float) (MathHelper.atan2(motion.x, motion.z) * (180.0D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(motion.z, f) * (180D / Math.PI));
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(source.getEntityId());
        buffer.writeItemStack(stack);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        int id = additionalData.readInt();
        Entity entity = world.getEntityByID(id);
        if(entity instanceof LivingEntity) {
            this.source = (LivingEntity) entity;
        }
        stack = additionalData.readItemStack();
    }

    public void checkForCollisions(List<BlockPos> checkedList) {
        Vec3d start = this.getPositionVec();
        Vec3d end = start.add(this.getMotion());
        BlockRayTraceResult rayTraceResult = world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        boolean collided = false;
        if(this.collided || rayTraceResult != null && rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos pos = rayTraceResult.getPos();
            BlockState state = world.getBlockState(pos);
            if(state.isOpaqueCube(world, pos)) {
                collided = true;
            } else if(state.getMaterial() == Material.GLASS) {
                // TODO gamerule
                if(!world.isRemote) {
                    world.destroyBlock(pos, false);
                    checkForCollisions(checkedList);
                }
            } else {
                if(checkedList.contains(pos)) {
                    return;
                }
                checkedList.add(pos);
                checkForCollisions(checkedList);
            }
        }
        EntityRayTraceResult entityRayTraceResult = entityRayTrace(start, end);
        if(entityRayTraceResult != null && entityRayTraceResult.getEntity() != null) {
            if(collided) {
                BlockPos pos = rayTraceResult.getPos();
                double blockDistance = getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
                double targetDistance = getDistanceSq(entityRayTraceResult.getEntity());
                if(targetDistance > blockDistance) {
                    this.onBlockHit(rayTraceResult);
                    return;
                }
            }
            onEntityHit(entityRayTraceResult);
        } else if(collided) {
            onBlockHit(rayTraceResult);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.checkForCollisions(new ArrayList<>());
        updateDirection();
        move(MoverType.SELF, getMotion());
    }

    public void onBlockHit(BlockRayTraceResult traceResult) {
        if(!world.isRemote) {
            remove();
        }
    }

    public void onEntityHit(EntityRayTraceResult traceResult) {
        if(!world.isRemote) {
            traceResult.getEntity().attackEntityFrom(DamageSource.GENERIC, damage);
            remove();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        int id = compound.getInt("shooterID");
        if(!(world.getEntityByID(id) instanceof LivingEntity)) {
            remove();
            return;
        }
        source = (LivingEntity) world.getEntityByID(id);
        stack = ItemStack.read(compound.contains("stack") ? compound.getCompound("stack") : new CompoundNBT());
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("shooterID", source.getEntityId());
        CompoundNBT stackData = stack.write(new CompoundNBT());
        compound.put("stack", stackData);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return false;
    }

    @Nullable
    protected EntityRayTraceResult entityRayTrace(Vec3d start, Vec3d end) {
        return ProjectileHelper.func_221271_a(this.world, this, start, end, this.getBoundingBox().expand(this.getMotion()).grow(1.0D), (entity) -> !entity.isSpectator() && entity.isAlive() && entity.canBeCollidedWith() && entity != source);
    }
}
