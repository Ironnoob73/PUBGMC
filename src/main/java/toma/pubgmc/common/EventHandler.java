package toma.pubgmc.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import toma.pubgmc.api.capability.BoostStats;
import toma.pubgmc.api.capability.PubgmcCapabilities;
import toma.pubgmc.common.capability.BoostStatsProvider;
import toma.pubgmc.util.ResourceUtil;

@Mod.EventBusSubscriber
public final class EventHandler {

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            event.addCapability(ResourceUtil.of("boost"), new BoostStatsProvider(player));
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        player.getCapability(PubgmcCapabilities.BOOST_STATS).ifPresent(BoostStats::sendToClient);
    }

    @SubscribeEvent
    public static void dimensionChangedEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        player.getCapability(PubgmcCapabilities.BOOST_STATS).ifPresent(BoostStats::sendToClient);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        player.getCapability(PubgmcCapabilities.BOOST_STATS).ifPresent(stats -> {
            if (!event.isEndConquered()) {
                stats.setBoostValue(0.0F);
            }
            stats.sendToClient();
        });
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        event.getEntity().getCapability(PubgmcCapabilities.BOOST_STATS)
                .ifPresent(newStats -> event.getOriginal().getCapability(PubgmcCapabilities.BOOST_STATS)
                        .ifPresent(oldStats -> newStats.deserializeNBT(oldStats.serializeNBT())));
    }
}
