package toma.pubgmc.api.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public final class PubgmcCapabilities {

    public static final Capability<BoostStats> BOOST_STATS = CapabilityManager.get(new CapabilityToken<>() {});
}
