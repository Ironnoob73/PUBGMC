package dev.toma.pubgmc.common.entity;

import dev.toma.pubgmc.games.object.LootGenerator;
import dev.toma.pubgmc.init.PMCBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class AirdropEntity extends Entity implements IEntityAdditionalSpawnData {

    private float fallSpeed;
    private AirdropType type;

    public AirdropEntity(EntityType<?> type, World world) {
        super(type, world);
        this.type = AirdropType.NORMAL;
        fallSpeed = 0.05F;
    }

    public AirdropEntity(World world, BlockPos pos, AirdropType type) {
        this(null, world);
        setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        this.type = type;
    }

    @Override
    public void tick() {
        if(fallSpeed <= 0) {
            fallSpeed = 0.05F;
        }
        Vec3d motion = getMotion();
        Vec3d actualMotion = new Vec3d(motion.x, -fallSpeed, motion.z);
        move(MoverType.SELF, actualMotion);
        super.tick();
        if(onGround) {
            if(type == AirdropType.NORMAL) {
                world.setBlockState(getPosition(), PMCBlocks.AIRDROP.getDefaultState(), 3);
            } else {
               world.setBlockState(getPosition(), PMCBlocks.FLARE_AIRDROP.getDefaultState(), 3);
            }
            TileEntity tileEntity = world.getTileEntity(getPosition());
            if(tileEntity instanceof LootGenerator) {
                ((LootGenerator) tileEntity).generateLoot();
            }
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
        type = AirdropType.values()[compound.getInt("airdropType")];
        fallSpeed = compound.getFloat("fallSpeed");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("airdropType", type.ordinal());
        compound.putFloat("fallSpeed", fallSpeed);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeEnumValue(type);
        buffer.writeFloat(fallSpeed);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        type = additionalData.readEnumValue(AirdropType.class);
        fallSpeed = additionalData.readFloat();
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    public AirdropType getAirdropType() {
        return type;
    }

    public enum AirdropType {
        NORMAL,
        FLARE
    }
}
