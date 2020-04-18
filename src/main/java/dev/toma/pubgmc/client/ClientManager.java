package dev.toma.pubgmc.client;

import dev.toma.pubgmc.Registry;
import dev.toma.pubgmc.client.render.entity.GliderRenderer;
import dev.toma.pubgmc.client.render.entity.ParachuteRenderer;
import dev.toma.pubgmc.client.render.entity.ThrowableRenderer;
import dev.toma.pubgmc.client.render.entity.UAZRenderer;
import dev.toma.pubgmc.common.entity.ParachuteEntity;
import dev.toma.pubgmc.common.entity.throwable.FlashEntity;
import dev.toma.pubgmc.common.entity.throwable.GrenadeEntity;
import dev.toma.pubgmc.common.entity.throwable.MolotovEntity;
import dev.toma.pubgmc.common.entity.throwable.SmokeEntity;
import dev.toma.pubgmc.common.entity.vehicle.AirDriveableEntity;
import dev.toma.pubgmc.common.entity.vehicle.LandDriveableEntity;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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
        RenderingRegistry.registerEntityRenderingHandler(LandDriveableEntity.UAZDriveable.class, UAZRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AirDriveableEntity.GliderDriveable.class, GliderRenderer::new);
    }

    public static void run(Supplier<Runnable> supplier) {
        DistExecutor.runWhenOn(Dist.CLIENT, supplier);
    }

    public static void playDelayedSoundAt(SoundEvent event, double x, double y, double z, float volume, float pitch) {
        run(() -> () -> {
            SimpleSound sound = new SimpleSound(event, SoundCategory.MASTER, volume, pitch, (float) x, (float) y, (float) z);
            Minecraft mc = Minecraft.getInstance();
            SoundHandler handler = mc.getSoundHandler();
            Entity entity = mc.getRenderViewEntity();
            double dist = UsefulFunctions.getDistance(x, y, z, entity.posX, entity.posY, entity.posZ);
            int ticks = (int) ((dist / 34) * 5);
            handler.playDelayed(sound, ticks);
        });
    }
}
