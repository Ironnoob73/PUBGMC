package dev.toma.pubgmc.common.block.crafting;

import dev.toma.pubgmc.common.block.PMCBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFactoryBlock extends PMCBlock {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<FactoryPart> PART = EnumProperty.create("part", FactoryPart.class);
    private final StructureObject structureObject = new StructureObject();

    public AbstractFactoryBlock(String name) {
        super(name, Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.5F));
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(PART, FactoryPart.GENERATOR));

        this.createStructureObject(this.structureObject);
    }

    public abstract void createStructureObject(StructureObject builder);

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(PART, FactoryPart.GENERATOR);
    }

    public Direction rotate(Direction direction, BlockState state) {
        int id = direction.getIndex();
        if(id < 2) {
            return direction;
        }
        return state.get(FACING).rotateYCCW();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        for(StructurePart part : this.structureObject.partList) {
            BlockPos p = pos;
            for(Direction d : part.relativePos) {
                p = p.offset(rotate(d, state));
            }
            worldIn.setBlockState(p, part.stateSupplier.get().with(FACING, state.get(FACING)));
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        for(StructurePart part : this.structureObject.partList) {
            BlockPos p = pos;
            for(Direction d : part.relativePos) {
                p = p.offset(rotate(d, state));
            }
            if(!worldIn.getBlockState(p).getMaterial().isReplaceable()) {
                return false;
            }
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return Minecraft.getInstance().gameSettings.fancyGraphics ? BlockRenderLayer.CUTOUT : BlockRenderLayer.SOLID;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        boolean flag = false;
        Direction[] offsets = null;
        for(StructurePart part : structureObject.partList) {
            BlockState state1 = part.stateSupplier.get();
            if(state1.get(PART) == state.get(PART)) {
                flag = true;
                offsets = part.relativePos;
                break;
            }
        }
        if(!flag) {
            for(StructurePart part : structureObject.partList) {
                BlockPos p = pos;
                for(Direction d : part.relativePos) {
                    p = p.offset(rotate(d, state));
                }
                worldIn.destroyBlock(p, false);
            }
        } else {
            BlockPos origin = pos;
            for(Direction d : offsets) {
                origin = origin.offset(rotate(d, state).getOpposite());
            }
            for(StructurePart part : structureObject.partList) {
                BlockPos p = origin;
                for(Direction d : part.relativePos) {
                    p = p.offset(rotate(d, state));
                }
                worldIn.destroyBlock(p, false);
            }
            worldIn.destroyBlock(origin, false);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    public enum FactoryPart implements IStringSerializable {

        GENERATOR,
        STORAGE,
        UI,
        PRODUCER;

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }
    }

    public static class StructureObject {
        private List<StructurePart> partList;

        private StructureObject() {
            this.partList = new ArrayList<>();
        }

        public StructureObject addPart(NonNullSupplier<BlockState> stateSupplier, Direction... directions) {
            Objects.requireNonNull(stateSupplier);
            Objects.requireNonNull(directions);
            this.partList.add(new StructurePart(stateSupplier, directions));
            return this;
        }
    }

    public static class StructurePart {

        private NonNullSupplier<BlockState> stateSupplier;
        private Direction[] relativePos;

        private StructurePart(NonNullSupplier<BlockState> stateSupplier, Direction... relativePos) {
            this.stateSupplier = stateSupplier;
            this.relativePos = relativePos;
        }
    }
}
