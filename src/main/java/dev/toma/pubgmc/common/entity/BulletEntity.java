package dev.toma.pubgmc.common.entity;

import dev.toma.pubgmc.Registry;
import dev.toma.pubgmc.util.object.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BulletEntity extends Entity {

    public Optional<Vec3d> origin = Optional.empty();

    public BulletEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public BulletEntity(World world, LivingEntity src, ItemStack stack) {
        this(Registry.PMCEntityTypes.BULLET, world);
        setPosition(src.posX, src.posY + src.getEyeHeight(), src.posZ);
        origin.map(this.getPositionVec());
        Vec3d vec3d = getVectorForRotation(src.rotationPitch, src.rotationYaw);
        double x = vec3d.x * 10.0F;
        double y = vec3d.y * 10.0F;
        double z = vec3d.z * 10.0F;
        setMotion(x, y, z);
    }

    @Override
    public void tick() {
        Vec3d start = this.getPositionVec();
        Vec3d end = start.add(this.getMotion());
        BlockRayTraceResult rayTraceResult = world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, this));
        if(rayTraceResult != null && rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
            this.remove();
        }
        super.tick();
        move(MoverType.SELF, getMotion());
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
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

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }
}
