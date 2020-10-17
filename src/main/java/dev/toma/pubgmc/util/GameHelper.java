package dev.toma.pubgmc.util;

import dev.toma.pubgmc.common.inventory.IHasInventory;
import dev.toma.pubgmc.games.interfaces.IGameManager;
import dev.toma.pubgmc.init.PMCBlocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GameHelper {

    public static void createDeathCrate(LivingEntity entity, IGameManager manager) {
        World world = entity.world;
        if(!world.isRemote && manager.createDeathCrate(entity) && !world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            BlockPos pos = entity.getPosition();
            if(!world.isAirBlock(pos)) {
                boolean isValid = false;
                for (Direction direction : Direction.values()) {
                    if(world.isAirBlock(pos.offset(direction))) {
                        pos = pos.offset(direction);
                        isValid = true;
                        break;
                    }
                }
                if(!isValid) {
                    while (!world.getFluidState(pos).isEmpty()) {
                        pos = pos.up();
                    }
                    if(!world.isAirBlock(pos)) {
                        pos = null;
                    }
                }
            }
            if(pos != null) {
                world.setBlockState(pos, PMCBlocks.DEATH_CRATE.getDefaultState(), 3);
                IItemHandler handler = world.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
                if(handler != null) {
                    if(entity instanceof PlayerEntity) {
                        PlayerInventory inventory = ((PlayerEntity) entity).inventory;
                        for (int i = 0; i < inventory.getSizeInventory(); i++) {
                            ItemStack stack = inventory.getStackInSlot(i);
                            if(!stack.isEmpty()) {
                                handler.insertItem(i, stack.copy(), false);
                            }
                        }
                        inventory.clear();
                    } else if(entity instanceof IHasInventory) {
                        ((IHasInventory) entity).transferTo(handler);
                    }
                }
            }
        }
    }
}
