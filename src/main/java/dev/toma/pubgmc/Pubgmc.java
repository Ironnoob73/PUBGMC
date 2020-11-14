package dev.toma.pubgmc;

import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.IWorldCap;
import dev.toma.pubgmc.capability.PMCInventoryHandler;
import dev.toma.pubgmc.capability.player.InventoryFactory;
import dev.toma.pubgmc.capability.player.InventoryProvider;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.capability.player.PlayerCapStorage;
import dev.toma.pubgmc.capability.world.WorldDataFactory;
import dev.toma.pubgmc.client.ClientManager;
import dev.toma.pubgmc.client.ModKeybinds;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.internal.InternalHandles;
import dev.toma.pubgmc.client.screen.*;
import dev.toma.pubgmc.command.GameCommand;
import dev.toma.pubgmc.command.LootCommand;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.config.Config;
import dev.toma.pubgmc.content.ContentManager;
import dev.toma.pubgmc.data.loadout.LoadoutManager;
import dev.toma.pubgmc.data.loot.LootManager;
import dev.toma.pubgmc.init.PMCContainers;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.data.recipe.FactoryRecipeManager;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Random;

import static dev.toma.pubgmc.init.PMCContainers.CONTAINER_TYPES;
import static dev.toma.pubgmc.init.PMCTileEntities.TE_TYPES;

@Mod(Pubgmc.MODID)
public class Pubgmc {

    public static final String MODID = "pubgmc";
    public static final Logger pubgmcLog = LogManager.getLogger("pubgmc");
    public static final Random rand = new Random();
    private static FactoryRecipeManager recipeManager;
    private static ContentManager contentManager;
    private static LootManager lootManager;
    private static LoadoutManager loadoutManager;
    public static GameRules.RuleKey<GameRules.BooleanValue> WEAPON_GRIEFING = GameRules.register("weaponGriefing", GameRules.BooleanValue.create(true));

    public Pubgmc() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forge = MinecraftForge.EVENT_BUS;
        CONTAINER_TYPES.register(modBus);
        TE_TYPES.register(modBus);
        modBus.addListener(this::setupClient);
        modBus.addListener(this::setupCommon);
        forge.addListener(this::serverInit);
        forge.addListener(this::serverStart);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON);
        Config.load(Config.CLIENT, FMLPaths.CONFIGDIR.get().resolve("pubgmc-client.toml"));
        Config.load(Config.COMMON, FMLPaths.CONFIGDIR.get().resolve("pubgmc-common.toml"));
        recipeManager = new FactoryRecipeManager();
        lootManager = new LootManager();
        contentManager = new ContentManager();
        loadoutManager = new LoadoutManager();
    }

    private void setupClient(FMLClientSetupEvent event) {
        ClientManager.registerEntityRenderers();
        ModKeybinds.init();
        DeferredWorkQueue.runLater(() -> {
            ScreenManager.registerFactory(PMCContainers.LOOT_SPAWNER.get(), LootSpawnerScreen::new);
            ScreenManager.registerFactory(PMCContainers.AIRDROP.get(), AirdropScreen::new);
            ScreenManager.registerFactory(PMCContainers.FLARE_AIRDROP.get(), FlareAirdropScreen::new);
            ScreenManager.registerFactory(PMCContainers.PLAYER_CONTAINER.get(), PMCPlayerInventoryScreen::new);
            ScreenManager.registerFactory(PMCContainers.ATTACHMENT_CONTAINER.get(), AttachmentScreen::new);
            ScreenManager.registerFactory(PMCContainers.FACTORY.get(), FactoryScreen::new);
            ScreenManager.registerFactory(PMCContainers.DEATH_CRATE.get(), DeathCrateScreen::new);
        });
        Animations.init();
        contentManager.start();
        if(Config.animationTool.get()) InternalHandles.init();
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        NetworkManager.init();
        CapabilityManager.INSTANCE.register(IPlayerCap.class, new PlayerCapStorage(), () -> new PlayerCapFactory(null));
        CapabilityManager.INSTANCE.register(PMCInventoryHandler.class, new InventoryProvider.Storage(), InventoryFactory::new);
        CapabilityManager.INSTANCE.register(IWorldCap.class, new WorldDataFactory.Storage(), WorldDataFactory::new);
    }

    private void serverInit(FMLServerAboutToStartEvent event) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        IReloadableResourceManager manager = server.getResourceManager();
        manager.addReloadListener(recipeManager);
        manager.addReloadListener(lootManager);
        manager.addReloadListener(loadoutManager);
    }

    private void serverStart(FMLServerStartingEvent event) {
        LootCommand.register(event.getCommandDispatcher());
        GameCommand.register(event.getCommandDispatcher());
    }

    public static ResourceLocation makeResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static void logBigError(String message, Object... objects) {
        pubgmcLog.error("--------------------[ ERROR ]--------------------");
        pubgmcLog.error("");
        pubgmcLog.error(message, objects);
        pubgmcLog.error("");
        pubgmcLog.error("-------------------------------------------------");
    }

    public static boolean isOutdated() {
        Optional<? extends ModContainer> container = ModList.get().getModContainerById(MODID);
        if(!container.isPresent()) {
            return false;
        }
        ModContainer modContainer = container.get();
        VersionChecker.CheckResult checkResult = VersionChecker.getResult(modContainer.getModInfo());
        VersionChecker.Status status = checkResult.status;
        return status == VersionChecker.Status.OUTDATED || status == VersionChecker.Status.BETA_OUTDATED;
    }

    public static FactoryRecipeManager getRecipeManager() {
        return recipeManager;
    }

    public static LootManager getLootManager() {
        return lootManager;
    }

    public static ContentManager getContentManager() {
        return contentManager;
    }

    public static LoadoutManager getLoadoutManager() {
        return loadoutManager;
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientModEventHandler {

        @SubscribeEvent
        public static void stitchTextures(TextureStitchEvent.Pre event) {
            if(event.getMap().getBasePath().equals("textures")) {
                event.addSprite(makeResource("slot/night_vision"));
                event.addSprite(makeResource("slot/ghillie"));
                event.addSprite(makeResource("slot/backpack"));
                event.addSprite(makeResource("slot/locked"));
                for (AttachmentCategory category : AttachmentCategory.values()) {
                    event.addSprite(makeResource("slot/" + category.name().toLowerCase()));
                }
            }
        }
    }
}
