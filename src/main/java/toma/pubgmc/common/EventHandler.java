package toma.pubgmc.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import toma.pubgmc.common.capability.BoostStatsProvider;
import toma.pubgmc.util.ResourceUtil;

@Mod.EventBusSubscriber
public final class EventHandler {

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            event.addCapability(ResourceUtil.fromPath("boost"), new BoostStatsProvider(player));
        }
    }
}
