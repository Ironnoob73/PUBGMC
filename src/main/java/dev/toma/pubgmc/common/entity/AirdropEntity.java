package dev.toma.pubgmc.common.entity;

import dev.toma.pubgmc.capability.world.WorldDataProvider;
import dev.toma.pubgmc.games.Game;
import dev.toma.pubgmc.games.interfaces.IKeyHolder;
import dev.toma.pubgmc.games.interfaces.ILootGenerator;
import dev.toma.pubgmc.init.PMCBlocks;
import dev.toma.pubgmc.init.PMCEntities;
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

public class AirdropEntity extends Entity implements IEntityAdditionalSpawnData, IKeyHolder {

    private float fallSpeed;
    private AirdropType type;
    private long gameID;

    public AirdropEntity(EntityType<?> type, World world) {
        super(type, world);
        this.type = AirdropType.NORMAL;
        fallSpeed = 0.05F;
    }

    public AirdropEntity(World world, BlockPos pos, AirdropType type) {
        this(PMCEntities.AIRDROP, world);
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
        if(!world.isAirBlock(getPosition().down())) {
            if(type == AirdropType.NORMAL) {
                world.setBlockState(getPosition(), PMCBlocks.AIRDROP.getDefaultState(), 3);
            } else {
               world.setBlockState(getPosition(), PMCBlocks.FLARE_AIRDROP.getDefaultState(), 3);
            }
            TileEntity tileEntity = world.getTileEntity(getPosition());
            if(tileEntity instanceof ILootGenerator) {
                ((ILootGenerator) tileEntity).generateLoot();
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
        gameID = compound.getLong("gameID");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("airdropType", type.ordinal());
        compound.putFloat("fallSpeed", fallSpeed);
        compound.putLong("gameID", gameID);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(gameID <= 0) return;
        world.getCapability(WorldDataProvider.CAP).ifPresent(cap -> {
            Game game = cap.getGame();
            if(game != null && game.isRunning()) {
                if(!game.test(gameID)) {
                    AirdropEntity.this.remove();
                }
            }
        });
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

    @Override
    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    @Override
    public long getGameID() {
        return gameID;
    }

    public AirdropType getAirdropType() {
        return type;
    }

    public enum AirdropType {
        NORMAL,
        FLARE
    }
}
