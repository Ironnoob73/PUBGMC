package toma.pubgmc;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.format.ConfigFormats;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toma.pubgmc.config.PubgmcConfig;

@Mod(Pubgmc.MODID)
public final class Pubgmc {

    public static final String MODID = "pubgmc";
    public static final Logger LOGGER = LogManager.getLogger(MODID.toUpperCase());
    private static PubgmcConfig config;

    public Pubgmc() {
        config = Configuration.registerConfig(PubgmcConfig.class, ConfigFormats.yaml()).getConfigInstance();
    }

    public static PubgmcConfig getConfig() {
        return config;
    }
}
