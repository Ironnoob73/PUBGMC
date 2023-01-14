package toma.pubgmc.config;

import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import toma.pubgmc.Pubgmc;

@Config(id = Pubgmc.MODID + "-client", group = Pubgmc.MODID)
public final class PubgmcClientConfig {

    @Configurable
    @Configurable.StringPattern("#[a-fA-F0-9]{1,8}")
    @Configurable.Gui.ColorValue(isARGB = true)
    @Configurable.Comment("Background color of boost overlay")
    public String boostOverlayColorBG = "#FF444444";

    @Configurable
    @Configurable.StringPattern("#[a-fA-F0-9]{1,8}")
    @Configurable.Gui.ColorValue(isARGB = true)
    @Configurable.Comment("Foreground color of boost overlay")
    public String boostOverlayColorFG = "#FFFF9900";
}
