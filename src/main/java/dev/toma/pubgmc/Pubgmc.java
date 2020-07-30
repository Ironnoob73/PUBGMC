package dev.toma.pubgmc;

import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.capability.player.PlayerCapStorage;
import dev.toma.pubgmc.client.ClientManager;
import dev.toma.pubgmc.client.ModKeybinds;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.animation.builder.BuilderMain;
import dev.toma.pubgmc.client.render.OverlayGameRenderer;
import dev.toma.pubgmc.config.Config;
import dev.toma.pubgmc.data.loot.LootManager;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.util.recipe.FactoryCraftingRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static dev.toma.pubgmc.Registry.PMCContainers.CONTAINER_TYPES;
import static dev.toma.pubgmc.Registry.PMCTileEntities.TE_TYPES;

@Mod(Pubgmc.MODID)
public class Pubgmc {

    public static final String MODID = "pubgmc";
    public static Logger pubgmcLog = LogManager.getLogger();
    public static final Random rand = new Random();

    public static FactoryCraftingRecipes recipeManager = new FactoryCraftingRecipes();
    public static LootManager lootManager = new LootManager();

    public static GameRules.RuleKey<GameRules.BooleanValue> WEAPON_GRIEFING = GameRules.register("weaponGriefing", GameRules.BooleanValue.create(true));

    public Pubgmc() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forge = MinecraftForge.EVENT_BUS;
        CONTAINER_TYPES.register(modBus);
        TE_TYPES.register(modBus);
        modBus.addListener(this::setupClient);
        modBus.addListener(this::setupCommon);
        forge.addListener(this::serverInit);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON);
        Config.load(Config.CLIENT, FMLPaths.CONFIGDIR.get().resolve("pubgmc-client.toml"));
        Config.load(Config.COMMON, FMLPaths.CONFIGDIR.get().resolve("pubgmc-common.toml"));
    }

    private void setupClient(FMLClientSetupEvent event) {
        ClientManager.loadEntityRenderers();
        ModKeybinds.init();
        DeferredWorkQueue.runLater(() -> {
            //ScreenManager.registerFactory(Registry.PMCContainers.WEAPON_FACTORY.get(), null);
        });
        Animations.init();
        if(Config.animationTool.get()) BuilderMain.init();
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        NetworkManager.init();
        CapabilityManager.INSTANCE.register(IPlayerCap.class, new PlayerCapStorage(), () -> new PlayerCapFactory(null));
    }

    private void serverInit(FMLServerAboutToStartEvent event) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        IReloadableResourceManager manager = server.getResourceManager();
        manager.addReloadListener(recipeManager);
        manager.addReloadListener(lootManager);
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
}
