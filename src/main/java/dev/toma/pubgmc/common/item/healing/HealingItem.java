package dev.toma.pubgmc.common.item.healing;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.Animation;
import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.animation.HandAnimate;
import dev.toma.pubgmc.common.item.PMCItem;
import dev.toma.pubgmc.common.item.utility.FuelCanItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class HealingItem extends PMCItem implements HandAnimate {

    protected HealingItem(String name, Properties properties) {
        super(name, properties);
    }

    public abstract String getSuccessKey();

    @Override
    public abstract int getUseDuration(ItemStack stack);

    // TODO make abstract and nonnull!
    @OnlyIn(Dist.CLIENT)
    public Animation getUseAnimation(ItemStack stack) {
        return null;
    }

    public String getFailKey() {
        return null;
    }

    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public int getInventoryLimit() {
        return 1;
    }

    public void onFinish(PlayerEntity player) {

    }

    @Override
    public final ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(this.canUse(playerIn)) {
            if(worldIn.isRemote) {
                Animation animation = this.getUseAnimation(stack);
                // TODO remove null check, every healing item must have animation
                if(animation != null) {
                    AnimationManager.playNewAnimation(Animations.HEALING, animation);
                }
            }
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        } else {
            sendErrMsg(playerIn);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public final UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public final void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if(player instanceof PlayerEntity) {
            if(!this.canUse((PlayerEntity) player)) {
                player.stopActiveHand();
                if(player.world.isRemote) AnimationManager.stopAnimation(Animations.HEALING);
                sendErrMsg((PlayerEntity) player);
            } else {
                if(!player.world.isRemote) ((PlayerEntity) player).sendStatusMessage(new StringTextComponent(FuelCanItem.numberFormatter.format(count / 20.0F) + "s"), true);
            }
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity livingEntity, int ticksLeft) {
        if(world.isRemote) {
            AnimationManager.stopAnimation(Animations.HEALING);
        }
    }

    @Override
    public final ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if(entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            if(!player.world.isRemote) {
                this.onFinish(player);
                if(this.getSuccessKey() != null) player.sendStatusMessage(new TranslationTextComponent(this.getSuccessKey()), true);
            }
            if(!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderRightArm() {
        GlStateManager.translatef(0.0F, 0.0F, 0.7F);
        renderHand(HandSide.RIGHT);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderLeftArm() {
        GlStateManager.translatef(0.0F, 0.0F, 0.7F);
        renderHand(HandSide.LEFT);
    }

    private void sendErrMsg(PlayerEntity entity) {
        if(entity.world.isRemote) return;
        String failKey = this.getFailKey();
        if(failKey == null) return;
        entity.sendStatusMessage(new TranslationTextComponent(failKey), true);
    }
}
