package dev.toma.pubgmc.common.block.crafting;

import dev.toma.pubgmc.common.block.PMCBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
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

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        for(StructurePart part : this.structureObject.partList) {
            // TODO correct directions based on facing
            BlockPos p = pos;
            for(Direction d : part.relativePos) {
                p = p.offset(d);
            }
            worldIn.setBlockState(p, part.stateSupplier.get());
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        for(StructurePart part : this.structureObject.partList) {
            // TODO correct directions based on facing
            BlockPos p = pos;
            for(Direction d : part.relativePos) {
                p = p.offset(d);
            }
            if(!worldIn.getBlockState(p).getMaterial().isReplaceable()) {
                return false;
            }
        }
        return true;
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
