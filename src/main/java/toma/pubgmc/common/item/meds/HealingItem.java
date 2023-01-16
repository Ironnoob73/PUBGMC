package toma.pubgmc.common.item.meds;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import toma.pubgmc.common.item.UseableItem;
import toma.pubgmc.util.time.TickTimeProvider;

import java.util.function.Predicate;

public class HealingItem extends UseableItem {

    private final float healAmount;
    private final Predicate<Player> useCheck;

    public HealingItem(float healAmount, Predicate<Player> useCheck, TickTimeProvider useDuration, Properties properties) {
        super(useDuration, properties);
        this.healAmount = healAmount;
        this.useCheck = useCheck;
    }

    public static Predicate<Player> notMaxHealth() {
        return player -> player.getHealth() < player.getMaxHealth();
    }

    public static Predicate<Player> underHealthPercent(float pct) {
        return player -> player.getHealth() < (player.getMaxHealth() * pct);
    }

    @Override
    public boolean canUse(Player player, Level level, ItemStack stack) {
        return this.useCheck.test(player);
    }

    @Override
    protected boolean shouldCheckUsageContinually(ItemStack stack, LivingEntity living, int count) {
        return true;
    }

    @Override
    public ItemStack onFinished(Player player, Level level, ItemStack stack) {
        float toHeal = this.getActualHealAmount(player, this.healAmount);
        player.heal(toHeal);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return stack;
    }

    protected float getActualHealAmount(Player player, float originalHealAmount) {
        return originalHealAmount;
    }
}
