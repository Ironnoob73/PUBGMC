package dev.toma.pubgmc.common.entity;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.Registry;
import dev.toma.pubgmc.common.item.wearable.IPMCArmor;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketBulletImpactParticle;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSpawnParticlePacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
        float dp = (rand.nextFloat() / 2.0F - rand.nextFloat() / 2.0F) * inaccuracy;
        float dy = (rand.nextFloat() / 2.0F - rand.nextFloat() / 2.0F) * inaccuracy;
        Vec3d vec3d = getVectorForRotation(src.rotationPitch + dp, src.rotationYaw + dy);
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
                if(!world.isRemote) {
                    if(world.getGameRules().getBoolean(Pubgmc.WEAPON_GRIEFING)) {
                        world.destroyBlock(pos, false);
                    } else {
                        if(checkedList.contains(pos)) {
                            return;
                        }
                        checkedList.add(pos);
                    }
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
        EntityRayTraceResult entityRayTraceResult = entityRayTrace(start, end, getBoundingBox().expand(getMotion()).grow(1.0D), (entity) -> !entity.isSpectator() && entity.isAlive() && entity.canBeCollidedWith() && entity != source);
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
        if(ticksExisted >= 100) {
            remove();
        }
        if(ticksExisted > ticks) {
            Vec3d motion = getMotion();
            setMotion(motion.x * 0.995D, motion.y - gravity, motion.z * 0.995D);
        }
        super.tick();
        this.checkForCollisions(new ArrayList<>());
        updateDirection();
        move(MoverType.SELF, getMotion());
    }

    public void onBlockHit(BlockRayTraceResult traceResult) {
        if(!world.isRemote) {
            BlockPos pos = traceResult.getPos();
            NetworkManager.sendToAll(world, new CPacketBulletImpactParticle(pos, traceResult.getHitVec()));
            SoundType soundType = world.getBlockState(pos).getSoundType();
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), soundType.getBreakSound(), SoundCategory.BLOCKS, 1.0F, soundType.getPitch() * 0.8F);
            remove();
        }
    }

    public void onEntityHit(EntityRayTraceResult traceResult) {
        if(!world.isRemote) {
            Entity entity = traceResult.getEntity();
            Vec3d hit = traceResult.getHitVec();
            if(entity instanceof LivingEntity) {
                float eyes = entity.getEyeHeight();
                boolean canHeadshot = eyes > 1.0D;
                boolean isHeadshot = canHeadshot && hit.y > entity.posY + eyes - 0.15d;
                if(isHeadshot) {
                    damage *= headshotMultiplier;
                }
                EquipmentSlotType slotType = isHeadshot ? EquipmentSlotType.HEAD : EquipmentSlotType.CHEST;
                LivingEntity target = (LivingEntity) entity;
                ItemStack stack = target.getItemStackFromSlot(slotType);
                if(stack.getItem() instanceof IPMCArmor) {
                    double multiplier = ((IPMCArmor) stack.getItem()).damageMultiplier();
                    stack.damageItem((int) damage, target, le -> {});
                    damage *= multiplier;
                }
            }
            for(ServerPlayerEntity player : ((ServerWorld) world).getPlayers()) {
                player.connection.sendPacket(new SSpawnParticlePacket(new BlockParticleData(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState()), true, (float) hit.x, (float) hit.y, (float) hit.z, 0.0F, 0.0F, 0.0F, 0.2F, (int) damage * 2));
            }
            entity.attackEntityFrom(DamageSource.GENERIC, damage);
            entity.hurtResistantTime = 0;
            remove();
        }
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
        ticks = compound.getInt("gravityStart");
        gravity = compound.getFloat("gravity");
        damage = compound.getFloat("damage");
        headshotMultiplier = compound.getFloat("headshotMultiplier");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("shooterID", source.getEntityId());
        CompoundNBT stackData = stack.write(new CompoundNBT());
        compound.put("stack", stackData);
        compound.putInt("gravityStart", ticks);
        compound.putFloat("gravity", gravity);
        compound.putFloat("damage", damage);
        compound.putFloat("headshotMultiplier", headshotMultiplier);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return false;
    }

    protected EntityRayTraceResult entityRayTrace(Vec3d start, Vec3d end, AxisAlignedBB aabb, Predicate<Entity> filter) {
        double distance = Double.MAX_VALUE;
        Entity entity = null;
        Vec3d vec = null;
        for(Entity e : world.getEntitiesInAABBexcluding(this, aabb, filter)) {
            AxisAlignedBB alignedBB = e.getBoundingBox();
            Optional<Vec3d> optionalVec3d = alignedBB.rayTrace(start, end);
            if(optionalVec3d.isPresent()) {
                Vec3d vec3d = optionalVec3d.get();
                double distanceToEntity = start.squareDistanceTo(vec3d);
                if(distanceToEntity < distance) {
                    entity = e;
                    distance = distanceToEntity;
                    vec = vec3d;
                }
            }
        }
        if(entity == null) {
            return null;
        } else {
            return new EntityRayTraceResult(entity, vec);
        }
    }
}
