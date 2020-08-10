package dev.toma.pubgmc.common.block;

import dev.toma.pubgmc.common.tileentity.AirdropTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class AirdropBlock extends PMCBlock {

    private final Supplier<? extends AirdropTileEntity> supplier;

    public AirdropBlock(String name, Supplier<? extends AirdropTileEntity> supplier) {
        super(name, Properties.create(Material.IRON).hardnessAndResistance(4.0F));
        this.supplier = supplier;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) worldIn.getTileEntity(pos), pos);
        }
        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return supplier.get();
    }
}
