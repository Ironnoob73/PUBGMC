package toma.pubgmc.common.item.meds;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import toma.pubgmc.api.capability.PubgmcCapabilities;
import toma.pubgmc.common.item.UseableItem;
import toma.pubgmc.util.time.TickTimeProvider;

public class BoostItem extends UseableItem {

    private final float boostAmount;

    public BoostItem(float boostAmount, TickTimeProvider useDuration, Properties properties) {
        super(useDuration, properties);
        this.boostAmount = boostAmount;
    }

    @Override
    public boolean canUse(Player player, Level level, ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack onFinished(Player player, Level level, ItemStack stack) {
        player.getCapability(PubgmcCapabilities.BOOST_STATS)
                .ifPresent(stats -> stats.addBoostValue(this.boostAmount));
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return stack;
    }
}
