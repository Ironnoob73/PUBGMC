package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.Registry;
import dev.toma.pubgmc.common.entity.ParachuteEntity;
import dev.toma.pubgmc.common.item.PMCItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ParachuteItem extends PMCItem {

    public ParachuteItem(String name) {
        super(name, new Properties().group(ITEMS).maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote) {
            if(playerIn.getRidingEntity() == null) {
                /*Entity entity = Registry.PMCEntityTypes.PARACHUTE.spawn(worldIn, null, playerIn, playerIn.getPosition(), SpawnReason.MOB_SUMMONED, true, false);
                //playerIn.startRiding(entity);*/
                ParachuteEntity entity = new ParachuteEntity(worldIn, playerIn);
                worldIn.addEntity(entity);
                playerIn.startRiding(entity);
                if(!playerIn.isCreative()) {
                    stack.shrink(1);
                }
                return ActionResult.newResult(ActionResultType.SUCCESS, stack);
            }
        }
        return ActionResult.newResult(ActionResultType.PASS, stack);
    }
}
