package dev.toma.pubgmc.client;

import dev.toma.pubgmc.Registry;
import dev.toma.pubgmc.client.render.entity.ParachuteRenderer;
import dev.toma.pubgmc.client.render.entity.ThrowableRenderer;
import dev.toma.pubgmc.common.entity.ParachuteEntity;
import dev.toma.pubgmc.common.entity.throwable.FlashEntity;
import dev.toma.pubgmc.common.entity.throwable.GrenadeEntity;
import dev.toma.pubgmc.common.entity.throwable.MolotovEntity;
import dev.toma.pubgmc.common.entity.throwable.SmokeEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.function.Supplier;

public class ClientManager {

    public static void loadEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(ParachuteEntity.class, ParachuteRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GrenadeEntity.class, manager -> new ThrowableRenderer<>(manager, () -> Registry.PMCItems.GRENADE));
        RenderingRegistry.registerEntityRenderingHandler(SmokeEntity.class, manager -> new ThrowableRenderer<>(manager, () -> Registry.PMCItems.SMOKE));
        RenderingRegistry.registerEntityRenderingHandler(FlashEntity.class, manager -> new ThrowableRenderer<>(manager, () -> Registry.PMCItems.FLASH));
        RenderingRegistry.registerEntityRenderingHandler(MolotovEntity.class, manager -> new ThrowableRenderer<>(manager, () -> Registry.PMCItems.MOLOTOV));
    }

    public static void run(Supplier<Runnable> supplier) {
        DistExecutor.runWhenOn(Dist.CLIENT, supplier);
    }
}
