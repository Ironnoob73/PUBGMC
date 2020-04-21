package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.client.render.item.VehicleSpawnerRenderer;
import dev.toma.pubgmc.common.entity.vehicle.DriveableEntity;
import dev.toma.pubgmc.common.item.PMCItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Function;

public class VehicleSpawnerItem extends PMCItem {

    private final IFactory<?> factory;

    public <T extends DriveableEntity> VehicleSpawnerItem(String name, IFactory<T> factory, Function<VehicleSpawnerRenderer, VehicleSpawnerRenderer> registerFunc) {
        super(name, new Properties().maxStackSize(1).group(ITEMS).setTEISR(() -> () -> registerFunc.apply(VehicleSpawnerRenderer.instance)));
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

    public interface IFactory<E extends DriveableEntity> {
        E create(World world, BlockPos pos);
    }
}
