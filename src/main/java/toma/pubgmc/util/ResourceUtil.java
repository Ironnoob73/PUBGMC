package toma.pubgmc.util;

import net.minecraft.resources.ResourceLocation;
import toma.pubgmc.Pubgmc;

public final class ResourceUtil {

    public static ResourceLocation fromPath(String path) {
        return new ResourceLocation(Pubgmc.MODID, path);
    }
}
