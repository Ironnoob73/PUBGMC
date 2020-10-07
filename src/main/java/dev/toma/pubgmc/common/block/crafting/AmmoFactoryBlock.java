package dev.toma.pubgmc.common.block.crafting;

import dev.toma.pubgmc.common.tileentity.AbstractFactoryTileEntity;
import dev.toma.pubgmc.init.PMCBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class AmmoFactoryBlock extends AbstractFactoryBlock {

    public AmmoFactoryBlock(String name) {
        super(name);
    }

    @Override
    public void createStructureObject(StructureObject builder) {
        builder.addPart(() -> PMCBlocks.AMMO_FACTORY.getDefaultState().with(PART, FactoryPart.PRODUCER), Direction.UP);
    }

    @Nullable
    @Override
    public AbstractFactoryTileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AbstractFactoryTileEntity.Ammo();
    }
}
