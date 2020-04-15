package dev.toma.pubgmc;

import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.capability.player.PlayerCapStorage;
import dev.toma.pubgmc.client.render.entity.ParachuteRenderer;
import dev.toma.pubgmc.common.entity.ParachuteEntity;
import dev.toma.pubgmc.config.ConfigImpl;
import dev.toma.pubgmc.network.NetworkManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toma.config.Config;

@Mod(Pubgmc.MODID)
public class Pubgmc {

    public static final String MODID = "pubgmc";
    protected static Logger pubgmcLog = LogManager.getLogger("PUBGMC");

    public Pubgmc() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::setupClient);
        modBus.addListener(this::setupCommon);

        Config.registerConfig(this.getClass(), ConfigImpl::new);
    }

    private void setupClient(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ParachuteEntity.class, ParachuteRenderer::new);
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        NetworkManager.init();
        CapabilityManager.INSTANCE.register(IPlayerCap.class, new PlayerCapStorage(), () -> new PlayerCapFactory(null));
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
