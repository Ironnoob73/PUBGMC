package dev.toma.pubgmc.common.block.crafting;

import dev.toma.pubgmc.init.PMCBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class WeaponFactoryBlock extends AbstractFactoryBlock {

    public WeaponFactoryBlock(String name) {
        super(name);
    }

    @Override
    public void createStructureObject(StructureObject builder) {
        builder
                .addPart(() -> PMCBlocks.WEAPON_FACTORY.getDefaultState().with(PART, FactoryPart.STORAGE), Direction.WEST)
                .addPart(() -> PMCBlocks.WEAPON_FACTORY.getDefaultState().with(PART, FactoryPart.UI), Direction.UP)
                .addPart(() -> PMCBlocks.WEAPON_FACTORY.getDefaultState().with(PART, FactoryPart.PRODUCER), Direction.UP, Direction.WEST);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote) {
            //NetworkHooks.openGui((ServerPlayerEntity) player, ContainerHolders.WEAPON_FACTORY, pos);
        }
        return true;
    }
}
