package toma.pubgmc.common.item.meds;

import net.minecraft.world.entity.player.Player;
import toma.pubgmc.util.time.TickTimeProvider;

public class PartialHealingItem extends HealingItem {

    public PartialHealingItem(float healAmount, float maxHealthPercentage, TickTimeProvider useTime, Properties properties) {
        super(healAmount, underHealthPercent(maxHealthPercentage), useTime, properties);
    }

    @Override
    protected float getActualHealAmount(Player player, float originalHealAmount) {
        float max = player.getMaxHealth();
        float current = player.getHealth();
        float limit = max * 0.75F;
        return Math.min(limit - current, originalHealAmount);
    }
}
