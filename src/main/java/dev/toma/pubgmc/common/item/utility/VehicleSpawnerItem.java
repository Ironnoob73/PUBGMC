package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.common.entity.vehicle.DriveableEntity;
import dev.toma.pubgmc.common.item.PMCItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VehicleSpawnerItem extends PMCItem {

    private final IFactory factory;

    public VehicleSpawnerItem(String name, IFactory factory, Item.Properties properties) {
        super(name, properties);
        this.factory = factory;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getItem();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        if(!world.isRemote) {
            world.addEntity(factory.create(world, pos));
        }
        if(!player.isCreative()) {
            stack.shrink(1);
        }
        return ActionResultType.PASS;
    }

    public interface IFactory {
        DriveableEntity create(World world, BlockPos pos);
    }
}
