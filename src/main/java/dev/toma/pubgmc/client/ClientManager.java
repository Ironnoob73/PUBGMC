package dev.toma.pubgmc.client;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.client.render.block.LootSpawnerRenderer;
import dev.toma.pubgmc.client.render.entity.*;
import dev.toma.pubgmc.common.entity.AirdropEntity;
import dev.toma.pubgmc.common.entity.ParachuteEntity;
import dev.toma.pubgmc.common.entity.throwable.FlashEntity;
import dev.toma.pubgmc.common.entity.throwable.GrenadeEntity;
import dev.toma.pubgmc.common.entity.throwable.MolotovEntity;
import dev.toma.pubgmc.common.entity.throwable.SmokeEntity;
import dev.toma.pubgmc.common.entity.vehicle.AirDriveableEntity;
import dev.toma.pubgmc.common.entity.vehicle.DriveableEntity;
import dev.toma.pubgmc.common.entity.vehicle.LandDriveableEntity;
import dev.toma.pubgmc.common.item.wearable.GhillieSuitItem;
import dev.toma.pubgmc.common.tileentity.LootSpawnerTileEntity;
import dev.toma.pubgmc.init.PMCItems;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.EntityTickableSound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

public class ClientManager {

    private static Framebuffer framebuffer;
    public static boolean needsShaderUpdate;
    public static boolean isRenderingPiP;
    public static boolean shouldRenderScopeOverlay;

    public static void setScopeRendering(boolean rendering) {
        shouldRenderScopeOverlay = rendering;
    }

    public static void updateFramebufferSize(MainWindow window) {
        if(window.getFramebufferWidth() != framebuffer.framebufferTextureWidth || window.getFramebufferHeight() != framebuffer.framebufferTextureHeight) {
            framebuffer.func_216491_a(window.getFramebufferWidth(), window.getFramebufferHeight(), Minecraft.IS_RUNNING_ON_MAC);
        }
    }

    public static Framebuffer getFramebuffer() {
        if(framebuffer == null) {
            MainWindow window = Minecraft.getInstance().mainWindow;
            framebuffer = new Framebuffer(window.getFramebufferWidth(), window.getFramebufferHeight(), true, Minecraft.IS_RUNNING_ON_MAC);
            framebuffer.func_216491_a(window.getFramebufferWidth(), window.getFramebufferHeight(), Minecraft.IS_RUNNING_ON_MAC);
        }
        return framebuffer;
    }

    public static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(ParachuteEntity.class, ParachuteRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(GrenadeEntity.class, manager -> new ThrowableRenderer<>(manager, () -> PMCItems.GRENADE));
        RenderingRegistry.registerEntityRenderingHandler(SmokeEntity.class, manager -> new ThrowableRenderer<>(manager, () -> PMCItems.SMOKE));
        RenderingRegistry.registerEntityRenderingHandler(FlashEntity.class, manager -> new ThrowableRenderer<>(manager, () -> PMCItems.FLASH));
        RenderingRegistry.registerEntityRenderingHandler(MolotovEntity.class, manager -> new ThrowableRenderer<>(manager, () -> PMCItems.MOLOTOV));
        RenderingRegistry.registerEntityRenderingHandler(LandDriveableEntity.UAZDriveable.class, UAZRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AirDriveableEntity.GliderDriveable.class, GliderRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AirdropEntity.class, AirdropRenderer::new);
        ClientRegistry.bindTileEntitySpecialRenderer(LootSpawnerTileEntity.class, new LootSpawnerRenderer());
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

    public static void playVehicleSound(DriveableEntity entity) {
        SoundHandler handler = Minecraft.getInstance().getSoundHandler();
        SoundEvent event = entity.getCurrentState().getSound(entity.getSoundStorage());
        if(entity.sound != null && handler.isPlaying(entity.sound)) {
            handler.stop(entity.sound);
        }
        EntityTickableSound tickableSound = new EntityTickableSound(event, SoundCategory.NEUTRAL, entity);
        entity.sound = tickableSound;
        handler.play(tickableSound);
    }

    @Mod.EventBusSubscriber(modid = Pubgmc.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ModEventHandler {

        @SubscribeEvent
        public static void onItemColorHandlerRegister(ColorHandlerEvent.Item event) {
            ItemColors colors = event.getItemColors();
            colors.register((stack, layer) -> ((GhillieSuitItem) stack.getItem()).getColor(stack), PMCItems.GHILLIE_SUIT);
        }
    }
}
