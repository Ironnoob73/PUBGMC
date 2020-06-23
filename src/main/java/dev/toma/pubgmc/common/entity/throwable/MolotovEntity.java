package dev.toma.pubgmc.common.entity.throwable;

import dev.toma.pubgmc.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MolotovEntity extends ThrowableEntity {

    public static final int FIRE_SPREAD_AMOUNT = 6;
    public static final int SPREAD_DELAY = 7;

    private MolotovFireSpreader fireSpreader;
    private List<MolotovFirePosEntry> burningBlocks;

    private int timesSpreaded;
    private int timeLeft;
    private int startedSpreadingAt;
    private boolean hasStartedSpreading;

    public MolotovEntity(EntityType<?> type, World world) {
        super(type, world);
        this.fireSpreader = new MolotovFireSpreader();
        this.timeLeft = 200;
    }

    public MolotovEntity(World world, LivingEntity thrower, EnumEntityThrowState state, int timeLeft) {
        super(Registry.PMCEntityTypes.MOLOTOV, world, thrower, state, Integer.MAX_VALUE);
        this.fireSpreader = new MolotovFireSpreader();
        this.timeLeft = 200;
    }

    @Override
    public void onThrowableTick() {
        this.setInvisible(this.isFrozen);
        if(world.isRemote && !isFrozen) {
            world.addParticle(ParticleTypes.FLAME, this.posX, this.posY, this.posZ, 0, 0, 0);
        }
        if(this.world.getBlockState(this.getPosition()).getMaterial().isLiquid() && !world.isRemote) {
            remove();
        }
        if(isFrozen) {
            if(hasStartedSpreading) {
                if((this.ticksExisted - this.startedSpreadingAt) % SPREAD_DELAY == 0 && timesSpreaded < FIRE_SPREAD_AMOUNT) {
                    ++this.timesSpreaded;
                    this.fireSpreader.spread(this, this.burningBlocks);
                }
            }
            if(burningBlocks != null && !burningBlocks.isEmpty()) {
                List<Entity> entities = this.world.getEntitiesInAABBexcluding(this,
                        new AxisAlignedBB(this.posX - FIRE_SPREAD_AMOUNT, this.posY - FIRE_SPREAD_AMOUNT, this.posZ - FIRE_SPREAD_AMOUNT, this.posX + FIRE_SPREAD_AMOUNT, this.posY + FIRE_SPREAD_AMOUNT, this.posZ + FIRE_SPREAD_AMOUNT),
                        e -> e.isAlive() && !e.isSpectator());
                for(Entity e : entities) {
                    if(e instanceof LivingEntity) {
                        for(MolotovFirePosEntry entry : this.burningBlocks) {
                            BlockPos p0 = new BlockPos(entry.pos.getX(), entry.y, entry.pos.getZ());
                            BlockPos p00 = new BlockPos(p0.getX(), p0.getY() + 1, p0.getZ());
                            BlockPos p1 = e.getPosition();
                            if(p0.equals(p1) || p00.equals(p1)) {
                                e.setFire(5);
                                if(this.ticksExisted % 10 == 0) {
                                    e.attackEntityFrom(DamageSource.ON_FIRE, 4);
                                }
                                break;
                            }
                        }
                    }
                }
                if(world.isRemote) {
                    burningBlocks.forEach(entry -> createMolotovParticles(world, entry, 2, 1));
                }
            }
            --this.timeLeft;
            if(timeLeft <= 0 && !world.isRemote) this.remove();
        }
    }

    @Override
    public void onExplode() {
    }

    @Override
    public boolean canBounce() {
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return super.hasNoGravity() || this.hasStartedSpreading;
    }

    @Override
    protected void onEntityFrozen() {
        this.burningBlocks = this.fireSpreader.startSpreading(this);
        this.hasStartedSpreading = true;
        this.startedSpreadingAt = this.ticksExisted;
        this.world.playSound(null, this.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.MASTER, 1.0F, 1.0F);
        super.onEntityFrozen();
    }

    protected void createMolotovParticles(World world, MolotovFirePosEntry entry, int amount, int spreadAmount) {
        BlockPos pos = entry.pos;
        for(int i = 0; i < amount; i++) {
            world.addParticle(ParticleTypes.FLAME, pos.getX() + smallDouble(spreadAmount), entry.y, pos.getZ() + smallDouble(spreadAmount), smallDouble(20) - smallDouble(20), 0.01, smallDouble(20) - smallDouble(20));
        }
    }

    protected double smallDouble(int modifier) {
        return rand.nextDouble() / modifier;
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("ticksExisted", this.ticksExisted);
        compound.putInt("timeLeft", this.timeLeft);
        compound.putInt("startedSpreadingAt", this.startedSpreadingAt);
        compound.putBoolean("hasStarted", this.hasStartedSpreading);
        if(this.burningBlocks != null && !this.burningBlocks.isEmpty()) {
            ListNBT list = new ListNBT();
            for(MolotovFirePosEntry entry : this.burningBlocks) {
                list.add(this.writeFireEntryToNBT(entry));
            }
            compound.put("burningBlocks", list);
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.ticksExisted = compound.getInt("ticksExisted");
        this.timeLeft = compound.getInt("timeLeft");
        this.startedSpreadingAt = compound.getInt("startedSpreadingAt");
        this.hasStartedSpreading = compound.getBoolean("hasStarted");
        this.fireSpreader = new MolotovFireSpreader();
        this.burningBlocks = new ArrayList<>();
        if(compound.contains("burningBlocks")) {
            ListNBT list = compound.getList("burningBlocks", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < list.size(); i++) {
                CompoundNBT nbt = list.getCompound(i);
                MolotovFirePosEntry entry = this.readEntryFromNBT(nbt);
                burningBlocks.add(entry);
            }
        }
    }

    protected CompoundNBT writeFireEntryToNBT(MolotovFirePosEntry entry) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("pos", NBTUtil.writeBlockPos(entry.pos));
        nbt.putFloat("y", entry.y);
        return nbt;
    }

    protected MolotovFirePosEntry readEntryFromNBT(CompoundNBT nbt) {
        BlockPos pos = NBTUtil.readBlockPos(nbt.getCompound("pos"));
        float y = nbt.getFloat("y");
        return new MolotovFirePosEntry(pos, y);
    }

    protected static class MolotovFireSpreader {

        static final Direction[] FACINGS = new Direction[] {Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST};

        public List<MolotovFirePosEntry> startSpreading(MolotovEntity molotov) {
            if(molotov.isInWater()) {
                molotov.remove();
                return Collections.emptyList();
            }
            List<MolotovFirePosEntry> list = new ArrayList<>();
            BlockPos initial = molotov.getPosition();
            BlockPos ground = this.findGround(molotov.world, initial);
            VoxelShape shape = molotov.world.getBlockState(ground).getCollisionShape(molotov.world, ground);
            if(molotov.world.isAirBlock(ground.up())) {
                list.add(new MolotovFirePosEntry(ground, shape));
            }
            return list;
        }

        public void spread(MolotovEntity molotov, List<MolotovFirePosEntry> list) {
            if(list == null) return;
            List<MolotovFirePosEntry> toBeAdded = new ArrayList<>();
            for(MolotovFirePosEntry entry : list) {
                for(Direction facing : FACINGS) {
                    BlockPos pos = entry.pos.offset(facing);
                    BlockPos ground = this.findGround(molotov.world, pos);
                    Pair<Integer, BlockPos> pair = this.getFireStepHeight(molotov.world, ground);
                    ground = pair.getRight();
                    int stepHeight = pair.getLeft();
                    if(stepHeight > 1) {
                        continue;
                    }
                    BlockState state = molotov.world.getBlockState(ground);
                    VoxelShape shape = state.getCollisionShape(molotov.world, ground);
                    if(state.getBlock() == Blocks.AIR || state.getMaterial().isLiquid() || !molotov.world.getFluidState(ground).isEmpty()) {
                        continue;
                    }
                    MolotovFirePosEntry entry1 = new MolotovFirePosEntry(ground, shape);
                    toBeAdded.add(entry1);
                }
            }
            toBeAdded.stream().filter(entry -> !list.contains(entry)).forEach(list::add);
        }

        public BlockPos findGround(World world, BlockPos pos) {
            BlockPos ground = pos;
            while (ground.getY() > 1 && world.isAirBlock(ground)) {
                ground = new BlockPos(ground.getX(), ground.getY() - 1, ground.getZ());
            }
            return ground;
        }

        public Pair<Integer, BlockPos> getFireStepHeight(World world, BlockPos pos) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(pos);
            int stepHeight = 0;
            while (!world.isAirBlock(mutableBlockPos.up()) && pos.getY() < 255) {
                ++stepHeight;
                mutableBlockPos.setY(mutableBlockPos.getY() + 1);
            }
            return Pair.of(stepHeight, mutableBlockPos.toImmutable());
        }
    }

    protected static class MolotovFirePosEntry {

        public BlockPos pos;
        public float y;

        public MolotovFirePosEntry(BlockPos pos) {
            this.pos = pos;
            this.y = pos.getY() + 0.25F;
        }

        public MolotovFirePosEntry(BlockPos pos, VoxelShape shape) {
            this.pos = pos;
            this.y = pos.getY() + (!shape.isEmpty() ? (float) shape.getBoundingBox().maxY : 0.0F);
        }

        public MolotovFirePosEntry(BlockPos pos, float y) {
            this.pos = pos;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this) {
                return true;
            }
            if(obj instanceof MolotovFirePosEntry) {
                MolotovFirePosEntry entry = (MolotovFirePosEntry) obj;
                return this.pos.equals(entry.pos) && this.y == entry.y;
            }
            return false;
        }
    }
}
