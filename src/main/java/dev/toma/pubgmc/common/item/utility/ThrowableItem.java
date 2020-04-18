package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.common.entity.throwable.ThrowableEntity;
import dev.toma.pubgmc.common.item.PMCItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class ThrowableItem extends PMCItem {

    private final int maxFuse;
    private final IFactory factory;

    public ThrowableItem(String name, int fuse, IFactory factory) {
        super(name, new Properties().maxStackSize(1).group(ITEMS));
        this.maxFuse = fuse;
        this.factory = factory;
    }

    public void startCooking(ItemStack stack) {
        if(!stack.hasTag()) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putBoolean("cooking", false);
            nbt.putInt("time", maxFuse);
            stack.setTag(nbt);
        } else {
            stack.getTag().putBoolean("cooking", true);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(!(entityIn instanceof PlayerEntity)) return;
        if(!worldIn.isRemote) {
            if(!stack.hasTag()) {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putBoolean("cooking", false);
                nbt.putInt("time", maxFuse);
                stack.setTag(nbt);
            } else {
                if(stack.getTag().getBoolean("cooking")) {
                    int left = stack.getTag().getInt("time");
                    if(left <= 0) {
                        worldIn.addEntity(factory.create(worldIn, (PlayerEntity) entityIn, ThrowableEntity.EnumEntityThrowState.FORCED, left));
                        ((PlayerEntity) entityIn).inventory.removeStackFromSlot(itemSlot);
                        return;
                    }
                    stack.getTag().putInt("time", left - 1) ;
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if(!(entity instanceof PlayerEntity)) return false;
        if(!entity.world.isRemote) {
            PlayerEntity player = (PlayerEntity) entity;
            int left = maxFuse;
            if(stack.hasTag() && stack.getTag().contains("time")) {
                left = stack.getTag().getInt("time");
            }
            entity.world.addEntity(factory.create(entity.world, player, ThrowableEntity.EnumEntityThrowState.SHORT, left));
            player.inventory.removeStackFromSlot(player.inventory.currentItem);
        }
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote) {
            int left = maxFuse;
            if(stack.hasTag() && stack.getTag().contains("time")) {
                left = stack.getTag().getInt("time");
            }
            worldIn.addEntity(factory.create(worldIn, playerIn, ThrowableEntity.EnumEntityThrowState.LONG, left));
            playerIn.inventory.removeStackFromSlot(playerIn.inventory.currentItem);
        }
        return ActionResult.newResult(ActionResultType.PASS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("Right-click: Long throw"));
        tooltip.add(new TranslationTextComponent("Left-click: Short throw"));
    }

    public interface IFactory {

        ThrowableEntity create(World world, LivingEntity owner, ThrowableEntity.EnumEntityThrowState throwState, int time);
    }
}
