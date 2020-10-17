package dev.toma.pubgmc.games.handlers;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.capability.world.WorldDataFactory;
import dev.toma.pubgmc.capability.world.WorldDataProvider;
import dev.toma.pubgmc.games.Game;
import dev.toma.pubgmc.games.interfaces.IKeyHolder;
import dev.toma.pubgmc.games.interfaces.IObjectManager;
import dev.toma.pubgmc.games.interfaces.IPlayerManager;
import dev.toma.pubgmc.games.interfaces.IZone;
import dev.toma.pubgmc.util.GameHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
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
                executeWhenRunning(event.world, Game::exec_GameTick);
            }
        }

        @SubscribeEvent
        public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
            World world = event.getPlayer().world;
            world.getCapability(WorldDataProvider.CAP).ifPresent(cap -> {
                Game game = cap.getGame();
                if(game != null) {
                    if(game.isRunning()) {
                        game.getPlayerManager().handleLogIn(event.getPlayer());
                    }
                    cap.sync();
                }
            });
        }

        @SubscribeEvent
        public static void onLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
            executeWhenRunning(event.getPlayer().world, game -> game.getPlayerManager().handleLogOut(event.getPlayer()));
        }

        @SubscribeEvent
        public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
            executeWhenRunning(event.getWorld(), game -> game.getObjectManager().handleEntityJoin(event.getEntity()));
        }

        @SubscribeEvent
        public static void onChunkLoad(ChunkEvent.Load event) {
            World world = (World) event.getChunk().getWorldForge();
            if(world == null || world.isRemote)
                return;
            world.getCapability(WorldDataProvider.CAP).ifPresent(cap -> {
                Game game = cap.getGame();
                if(game != null && game.isRunning()) {
                    game.getObjectManager().handleChunkLoad(event.getChunk());
                }
            });
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            executeWhenRunning(event.getPlayer().world, game -> {
                IPlayerManager manager = game.getPlayerManager();
                manager.handlePlayerRespawn(event.getPlayer(), game.getStorage());
            });
        }

        @SubscribeEvent
        public static void onPlayerDeath(LivingDeathEvent event) {
            if(event.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                executeWhenRunning(player.world, game -> {
                    IPlayerManager manager = game.getPlayerManager();
                    manager.handlePlayerDeath(player, event.getSource());
                    if(game.createDeathCrate(player))
                        GameHelper.createDeathCrate(player, game);
                });
            } else if(event.getEntity() instanceof IKeyHolder) {
                IKeyHolder keyHolder = (IKeyHolder) event.getEntity();
                executeWhenRunning(event.getEntity().world, game -> {
                    if(keyHolder.test(game)) {
                        IObjectManager manager = game.getObjectManager();
                        if(game.createDeathCrate(event.getEntityLiving()))
                            GameHelper.createDeathCrate(event.getEntityLiving(), game);
                        // TODO
                    }
                });
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, value = Dist.CLIENT)
    public static class Client {

        @SubscribeEvent
        public static void renderWorldEvent(RenderWorldLastEvent event) {
            ClientWorld world = Minecraft.getInstance().world;
            executeWhenRunning(world, game -> {
                IZone zone = game.getZone();
                zone.getRenderer().doRender(zone, event.getPartialTicks());
            });
        }

        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void renderOverlayPost(RenderGameOverlayEvent event) {
            if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                Minecraft.getInstance().world.getCapability(WorldDataProvider.CAP).ifPresent(cap -> {
                    Game game = cap.getGame();
                    if(game != null && game.isRunning()) {
                        game.renderGameOverlay(event.getWindow(), event.getPartialTicks());
                    }
                });
            }
        }

        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent event) {
            Minecraft mc = Minecraft.getInstance();
            ClientWorld world = mc.world;
            if(world == null || event.phase == TickEvent.Phase.START || mc.isGamePaused())
                return;
            world.getCapability(WorldDataProvider.CAP).ifPresent(cap -> {
                Game game = cap.getGame();
                if(game != null && game.isRunning()) {
                    game.exec_GameTick();
                }
            });
        }
    }
}
