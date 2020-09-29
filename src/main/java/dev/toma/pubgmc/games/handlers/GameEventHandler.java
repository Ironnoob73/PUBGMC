package dev.toma.pubgmc.games.handlers;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.capability.WorldDataFactory;
import dev.toma.pubgmc.games.Game;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.function.Consumer;

public class GameEventHandler {

    static Optional<Game> getGame(World world) {
        return Optional.ofNullable(WorldDataFactory.getData(world).getGame());
    }

    static void executeWhenRunning(World world, Consumer<Game> action) {
        getGame(world).ifPresent(game -> {
            if(game.isRunning()) {
                action.accept(game);
            }
        });
    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID)
    public static class Common {

        @SubscribeEvent
        public static void onWorldTick(TickEvent.WorldTickEvent event) {
            if(event.phase == TickEvent.Phase.END) {
                executeWhenRunning(event.world, Game::onTick);
            }
        }

        @SubscribeEvent
        public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
            executeWhenRunning(event.getPlayer().world, game -> game.getPlayerManager().handleLogIn(event.getPlayer()));
        }

        @SubscribeEvent
        public static void onLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
            executeWhenRunning(event.getPlayer().world, game -> game.getPlayerManager().handleLogOut(event.getPlayer()));
        }
    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, value = Dist.CLIENT)
    public static class Client {

    }
}
