package toma.pubgmc.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import toma.pubgmc.util.time.TickTimeProvider;

public abstract class UseableItem extends Item {

    private final int useTime;

    public UseableItem(TickTimeProvider useDuration, Properties properties) {
        super(properties);
        this.useTime = useDuration.get();
    }

    public abstract boolean canUse(Player player, Level level, ItemStack stack);

    public abstract ItemStack onFinished(Player player, Level level, ItemStack stack);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.canUse(player, level, stack)) {
            player.startUsingItem(hand);
            return this.onUseCheckSuccess(level, player, hand, stack);
        } else {
            return this.onUseCheckFail(level, player, hand, stack);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if (living instanceof Player player) {
            if (this.canUse(player, level, stack)) {
                return this.onFinished(player, level, stack);
            }
        }
        return stack;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
        if (this.shouldCheckUsageContinually(stack, living, count)) {
            if (living instanceof Player player) {
                if (!this.canUse(player, player.level, stack)) {
                    this.onUsageAborted(player, stack, count);
                    player.stopUsingItem();
                }
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.useTime;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CUSTOM;
    }

    protected boolean shouldCheckUsageContinually(ItemStack stack, LivingEntity living, int count) {
        return false;
    }

    protected InteractionResultHolder<ItemStack> onUseCheckFail(Level level, Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResultHolder.pass(stack);
    }

    protected InteractionResultHolder<ItemStack> onUseCheckSuccess(Level level, Player player, InteractionHand hand, ItemStack stack) {
        return InteractionResultHolder.consume(stack);
    }

    protected void onUsageAborted(Player player, ItemStack stack, int remainingCount) {
    }
}
