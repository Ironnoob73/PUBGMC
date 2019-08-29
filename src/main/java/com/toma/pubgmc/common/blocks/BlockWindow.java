package com.toma.pubgmc.common.blocks;

import com.toma.pubgmc.common.network.PacketHandler;
import com.toma.pubgmc.common.network.sp.PacketParticle;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWindow extends PMCBlock {

    public static final PropertyEnum<EnumWindowAxis> AXIS = PropertyEnum.create("axis", EnumWindowAxis.class);
    public static final PropertyEnum<EnumWindowPart> PART = PropertyEnum.create("part", EnumWindowPart.class);
    public static final PropertyBool BROKEN = PropertyBool.create("broken");
    private static final AxisAlignedBB[] BOUNDING_BOX = {new AxisAlignedBB(0.35, 0, 0, 0.65, 1, 1),new AxisAlignedBB(0, 0, 0.35, 1, 1, 0.65)};
    private static final AxisAlignedBB[] COLLISION_BOX = {new AxisAlignedBB(0.4, 0, 0, 0.6, 1, 1),new AxisAlignedBB(0, 0, 0.4, 1, 1, 0.6)};
    private final WindowType windowType;

    public BlockWindow(String name, WindowType windowType) {
        super(name, Material.WOOD);
        this.setSoundType(SoundType.GLASS);
        this.setHardness(0.75f);
        this.windowType = windowType;
        this.setDefaultState(blockState.getBaseState().withProperty(BROKEN, false).withProperty(AXIS, EnumWindowAxis.NS).withProperty(PART, EnumWindowPart.LOWER_LEFT));
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        boolean b = blockAccess.getBlockState(pos.offset(side)).getBlock() == this;
        return b ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    public void neighborBroken(EnumFacing side, World world, BlockPos pos, IBlockState state) {
        if(!state.getValue(BROKEN)) {
            world.setBlockState(pos, state.withProperty(BROKEN, true));
            this.notifyNeighboringWindows(pos, world);
            if(!world.isRemote) {
                if(state.getValue(PART).ordinal() < 2) {
                    int axis = state.getValue(AXIS).ordinal();
                    PacketParticle packet = new PacketParticle(EnumParticleTypes.BLOCK_CRACK, 15, pos.getX(), pos.getY(), pos.getZ(), this, PacketParticle.ParticleAction.CREATE_LINE, axis);
                    PacketHandler.sendToAllClients(packet);
                }
            }
        }
    }

    public void breakWindow(IBlockState state, BlockPos pos, World world) {
        if(!state.getValue(BROKEN)) {
            world.setBlockState(pos, state.withProperty(BROKEN, true));
            this.notifyNeighboringWindows(pos, world);
            world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 3.0F, 1.0F);
        }
    }

    public void setToAir(World world, BlockPos pos) {
        world.setBlockToAir(pos);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        this.breakWindow(state, pos, worldIn);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX[state.getValue(AXIS).ordinal()];
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        if (blockState.getValue(BROKEN)) {
            return NULL_AABB;
        }
        return COLLISION_BOX[blockState.getValue(AXIS).ordinal()];
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        switch(windowType) {
            case WINDOW_1X2: {
                EnumWindowPart part = state.getValue(PART);
                if (part == EnumWindowPart.LOWER_LEFT) {
                    setToAir(worldIn, pos.up());
                } else {
                    setToAir(worldIn, pos.down());
                }
                break;
            }
            case WINDOW_2X1: {
                if(state.getValue(AXIS) == EnumWindowAxis.NS) {
                    IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.WEST));
                    if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                        setToAir(worldIn, pos.offset(EnumFacing.WEST));
                    } else {
                        setToAir(worldIn, pos.offset(EnumFacing.EAST));
                    }
                } else {
                    IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.NORTH));
                    if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                        setToAir(worldIn, pos.offset(EnumFacing.NORTH));
                    } else {
                        setToAir(worldIn, pos.offset(EnumFacing.SOUTH));
                    }
                }
                break;
            }
            case WINDOW_2X2: {
                if(state.getValue(PART).isLower()) {
                    setToAir(worldIn, pos.up());
                    if(state.getValue(AXIS) == EnumWindowAxis.NS) {
                        IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.WEST));
                        if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                            setToAir(worldIn, pos.offset(EnumFacing.WEST));
                            setToAir(worldIn, pos.offset(EnumFacing.WEST).up());
                        } else {
                            setToAir(worldIn, pos.offset(EnumFacing.EAST));
                            setToAir(worldIn, pos.offset(EnumFacing.EAST).up());
                        }
                    } else {
                        IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.NORTH));
                        if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                            setToAir(worldIn, pos.offset(EnumFacing.NORTH));
                            setToAir(worldIn, pos.offset(EnumFacing.NORTH).up());
                        } else {
                            setToAir(worldIn, pos.offset(EnumFacing.SOUTH));
                            setToAir(worldIn, pos.offset(EnumFacing.SOUTH).up());
                        }
                    }
                } else {
                    setToAir(worldIn, pos.down());
                    if(state.getValue(AXIS) == EnumWindowAxis.NS) {
                        IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.NORTH));
                        if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                            setToAir(worldIn, pos.offset(EnumFacing.NORTH));
                            setToAir(worldIn, pos.offset(EnumFacing.NORTH).down());
                        } else {
                            setToAir(worldIn, pos.offset(EnumFacing.SOUTH));
                            setToAir(worldIn, pos.offset(EnumFacing.SOUTH).down());
                        }
                    } else {
                        IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.WEST));
                        if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                            setToAir(worldIn, pos.offset(EnumFacing.WEST));
                            setToAir(worldIn, pos.offset(EnumFacing.WEST).down());
                        } else {
                            setToAir(worldIn, pos.offset(EnumFacing.EAST));
                            setToAir(worldIn, pos.offset(EnumFacing.EAST).down());
                        }
                    }
                }
                break;
            }
            default: case WINDOW_1X1: {
                super.breakBlock(worldIn, pos, state);
                break;
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing f = placer.getHorizontalFacing();
        IBlockState iBlockState = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(AXIS, EnumWindowAxis.getAxisFromFacing(f)).withProperty(PART, EnumWindowPart.LOWER_LEFT);
        switch (windowType) {
            case WINDOW_1X1: {
                return iBlockState;
            }
            case WINDOW_1X2: {
                if (world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up())) {
                    world.setBlockState(pos.up(), iBlockState.withProperty(PART, EnumWindowPart.UPPER_LEFT));
                    return iBlockState;
                }
                return Blocks.AIR.getDefaultState();
            }
            case WINDOW_2X1: {
                BlockPos neighbor = pos.offset(f.rotateY());
                if(world.getBlockState(neighbor).getBlock().isReplaceable(world, neighbor)) {
                    world.setBlockState(neighbor, iBlockState.withProperty(PART, EnumWindowPart.LOWER_RIGHT));
                    return iBlockState;
                }
                return Blocks.AIR.getDefaultState();
            }
            case WINDOW_2X2: {
                BlockPos neighbor = pos.offset(f.rotateY());
                IBlockState lr = world.getBlockState(neighbor);
                IBlockState ul = world.getBlockState(pos.up());
                IBlockState ur = world.getBlockState(neighbor.up());
                if(world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up()) &&
                    world.getBlockState(neighbor).getBlock().isReplaceable(world, neighbor) &&
                    world.getBlockState(neighbor.up()).getBlock().isReplaceable(world, neighbor.up())) {
                    world.setBlockState(pos.up(), iBlockState.withProperty(PART, EnumWindowPart.UPPER_LEFT));
                    world.setBlockState(neighbor, iBlockState.withProperty(PART, EnumWindowPart.LOWER_RIGHT));
                    world.setBlockState(neighbor.up(), iBlockState.withProperty(PART, EnumWindowPart.UPPER_RIGHT));
                    return iBlockState;
                }
                return Blocks.AIR.getDefaultState();
            }
            default: return Blocks.AIR.getDefaultState();
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(PART).ordinal();
        meta = state.getValue(BROKEN) ? meta | 4 : meta;
        meta |= state.getValue(AXIS).ordinal() << 3;
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(PART, EnumWindowPart.values()[meta%4]).withProperty(BROKEN, (meta & 4) > 0).withProperty(AXIS, EnumWindowAxis.values()[meta >> 3]);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PART, AXIS, BROKEN);
    }

    private void notifyNeighboringWindows(BlockPos pos, World world) {
        for(EnumFacing facing : EnumFacing.values()) {
            BlockPos neighborPos = pos.offset(facing);
            IBlockState neighbor = world.getBlockState(neighborPos);
            if(neighbor.getBlock() instanceof BlockWindow) {
                neighborBroken(facing, world, neighborPos, neighbor);
            }
        }
    }

    public enum WindowType {
        WINDOW_1X1,
        WINDOW_1X2,
        WINDOW_2X1,
        WINDOW_2X2
    }

    public enum EnumWindowAxis implements IStringSerializable {
        NS,
        WE;

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }

        public static EnumWindowAxis getAxisFromFacing(EnumFacing facing) {
            switch (facing) {
                case EAST: case WEST: return NS;
                case NORTH: case SOUTH: default: return WE;
            }
        }
    }

    public enum EnumWindowPart implements IStringSerializable {
        UPPER_RIGHT,
        UPPER_LEFT,
        LOWER_RIGHT,
        LOWER_LEFT;

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }

        public boolean isLower() {
            return this.ordinal() > 1;
        }

        public EnumWindowPart getOpposite() {
            return getOpposite(this);
        }

        public static EnumWindowPart getOpposite(EnumWindowPart part) {
            switch (part) {
                case LOWER_LEFT: return LOWER_RIGHT;
                case LOWER_RIGHT: default: return LOWER_LEFT;
                case UPPER_LEFT: return UPPER_RIGHT;
                case UPPER_RIGHT: return UPPER_LEFT;
            }
        }
    }
}
