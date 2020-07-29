package dev.toma.pubgmc.common.block;

import dev.toma.pubgmc.common.tileentity.LootSpawnerTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LootSpawnerBlock extends PMCBlock {

    public LootSpawnerBlock(String name) {
        super(name, Properties.create(Material.ROCK).hardnessAndResistance(-1.0F, 3600000.0F).noDrops());
    }

    @Override
    public boolean onBlockActivated(BlockState p_220051_1_, World p_220051_2_, BlockPos p_220051_3_, PlayerEntity p_220051_4_, Hand p_220051_5_, BlockRayTraceResult p_220051_6_) {
        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LootSpawnerTileEntity();
    }
}
