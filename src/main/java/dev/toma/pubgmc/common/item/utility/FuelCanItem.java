package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.common.entity.vehicle.DriveableEntity;
import dev.toma.pubgmc.common.item.PMCItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.text.DecimalFormat;

public class FuelCanItem extends PMCItem {

    public static DecimalFormat numberFormatter = new DecimalFormat("##0.0");

    public FuelCanItem(String name) {
        super(name, new Properties().maxStackSize(1).group(ITEMS));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 120;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        ITextComponent component = this.canUseItem(entityLiving);
        if(component == null) {
            DriveableEntity vehicle = (DriveableEntity) entityLiving.getRidingEntity();
            vehicle.refuel();
            if(entityLiving instanceof PlayerEntity && !((PlayerEntity) entityLiving).isCreative()) {
                stack.shrink(1);
            }
        } else {
            if(entityLiving instanceof PlayerEntity) {
                ((PlayerEntity) entityLiving).sendStatusMessage(component, true);
            }
        }
        return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        ITextComponent component = this.canUseItem(playerIn);
        if(component == null) {
            playerIn.setActiveHand(handIn);
        } else {
            if(!worldIn.isRemote) playerIn.sendStatusMessage(component, true);
            return ActionResult.newResult(ActionResultType.FAIL, stack);
        }
        return ActionResult.newResult(ActionResultType.PASS, stack);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        ITextComponent component = this.canUseItem(player);
        if(component != null) {
            player.stopActiveHand();
            if(player instanceof PlayerEntity) {
                ((PlayerEntity) player).sendStatusMessage(component, true);
            }
        } else {
            if(player instanceof PlayerEntity) {
                if(!player.world.isRemote) ((PlayerEntity) player).sendStatusMessage(new StringTextComponent(numberFormatter.format(count / 20.0F) + "s"), true);
            }
        }
    }

    protected TranslationTextComponent canUseItem(Entity entity) {
        if(entity.getRidingEntity() instanceof DriveableEntity) {
            DriveableEntity vehicle = (DriveableEntity) entity.getRidingEntity();
            if(vehicle.isMoving()) {
                return new TranslationTextComponent("pubgmc.fuelcan.fail.movement");
            }
            return null;
        }
        return new TranslationTextComponent("pubgmc.fuelcan.fail.vehicle");
    }
}
