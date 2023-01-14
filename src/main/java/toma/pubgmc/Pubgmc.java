package toma.pubgmc;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.format.ConfigFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toma.pubgmc.client.PubgmcClient;
import toma.pubgmc.common.Registry;
import toma.pubgmc.config.PubgmcConfig;
import toma.pubgmc.network.Networking;

@Mod(Pubgmc.MODID)
public final class Pubgmc {

    public static final String MODID = "pubgmc";
    public static final Logger LOGGER = LogManager.getLogger(MODID.toUpperCase());
    private static PubgmcConfig config;

    public Pubgmc() {
        config = Configuration.registerConfig(PubgmcConfig.class, ConfigFormats.yaml()).getConfigInstance();

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> PubgmcClient::new);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(Registry::onRegister);
        modEventBus.addListener(this::setup);
    }

    public static PubgmcConfig getConfig() {
        return config;
    }

    private void setup(FMLCommonSetupEvent event) {
        Networking.MessageRegistry.registerMessages();
    }
}
