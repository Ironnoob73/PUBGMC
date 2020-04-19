package dev.toma.pubgmc.common.item.healing;

import com.google.common.base.Preconditions;
import dev.toma.pubgmc.common.item.PMCItem;
import dev.toma.pubgmc.common.item.utility.FuelCanItem;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HealingItem extends PMCItem {

    private final Predicate<PlayerEntity> canUse;
    private final int useDuration;
    private final UseAction useAction;
    private final Consumer<PlayerEntity> onFinish;
    private final String successKey, failKey;

    protected HealingItem(String name, Builder builder) {
        super(name, new Item.Properties().group(ITEMS).maxStackSize(builder.stackSize));
        this.canUse = builder.canUse;
        this.useDuration = builder.useDuration;
        this.useAction = builder.useAction;
        this.onFinish = builder.onFinish;
        this.successKey = builder.successKey;
        this.failKey = builder.failKey;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(canUse.test(playerIn)) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
        } else {
            sendErrMsg(playerIn);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return useDuration;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return useAction;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if(player instanceof PlayerEntity) {
            if(!canUse.test((PlayerEntity) player)) {
                player.stopActiveHand();
                sendErrMsg((PlayerEntity) player);
            } else {
                if(!player.world.isRemote) ((PlayerEntity) player).sendStatusMessage(new StringTextComponent(FuelCanItem.numberFormatter.format(count / 20.0F) + "s"), true);
            }
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if(entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            if(!player.world.isRemote) {
                this.onFinish.accept(player);
                if(successKey != null) player.sendStatusMessage(new TranslationTextComponent(successKey), true);
            }
            if(!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return stack;
    }

    private void sendErrMsg(PlayerEntity entity) {
        if(entity.world.isRemote) return;
        if(failKey == null) return;
        entity.sendStatusMessage(new TranslationTextComponent(failKey), true);
    }

    public static class Builder {

        private int stackSize;
        private Predicate<PlayerEntity> canUse;
        private int useDuration;
        private UseAction useAction = UseAction.NONE;
        private Consumer<PlayerEntity> onFinish;
        private String successKey, failKey;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder stackSize(int n) {
            this.stackSize = n;
            return this;
        }

        public Builder useDuration(int ticks) {
            this.useDuration = ticks;
            return this;
        }

        public Builder canUse(Predicate<PlayerEntity> predicate) {
            this.canUse = predicate;
            return this;
        }

        public Builder useAction(UseAction useAction) {
            this.useAction = Objects.requireNonNull(useAction, "Use action cannot be null!");
            return this;
        }

        public Builder onFinish(Consumer<PlayerEntity> onFinish) {
            this.onFinish = onFinish;
            return this;
        }

        public Builder messages(String successKey, String failKey) {
            this.successKey = successKey;
            this.failKey = failKey;
            return this;
        }

        public HealingItem build(String name) {
            Preconditions.checkState(name != null && !name.isEmpty(), "Registry name cannot be null/empty!");
            stackSize = UsefulFunctions.wrap(stackSize, 1, 64);
            canUse = canUse != null ? canUse : UsefulFunctions.alwaysTruePredicate();
            useDuration = UsefulFunctions.wrap(useDuration, 1, 1180);
            Preconditions.checkNotNull(onFinish, "On finish action cannot be null!");
            return new HealingItem(name, this);
        }
    }
}
