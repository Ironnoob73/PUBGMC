package dev.toma.pubgmc.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

public class PMCHorizontalBlock extends PMCBlock {

    public static final DirectionProperty FACE = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public PMCHorizontalBlock(String name, Properties properties) {
        super(name, properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACE, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(FACE, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACE);
    }
}
