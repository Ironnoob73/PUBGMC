package toma.pubgmc.client;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.format.ConfigFormats;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import toma.pubgmc.client.render.overlay.BoostOverlay;
import toma.pubgmc.config.PubgmcClientConfig;

public class PubgmcClient {

    public static PubgmcClient INSTANCE;
    private static PubgmcClientConfig config;

    public PubgmcClient() {
        if (INSTANCE != null)
            throw new IllegalStateException();
        INSTANCE = this;

        config = Configuration.registerConfig(PubgmcClientConfig.class, ConfigFormats.yaml()).getConfigInstance();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::registerHudOverlays);
    }

    public static PubgmcClientConfig getConfig() {
        return config;
    }

    private void setup(FMLClientSetupEvent event) {

    }

    private void registerHudOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelow(new ResourceLocation("player_health"), "boost", new BoostOverlay());
    }
}
