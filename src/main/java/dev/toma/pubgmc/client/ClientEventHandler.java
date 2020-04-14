package dev.toma.pubgmc.client;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.BoostStats;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.config.ConfigImpl;
import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Pubgmc.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void renderOverlayPre(RenderGameOverlayEvent.Pre event) {
        if(ConfigImpl.client.specialHUDRender) {
            if(event.getType() == RenderGameOverlayEvent.ElementType.ARMOR
                    || event.getType() == RenderGameOverlayEvent.ElementType.HEALTH
                    || event.getType() == RenderGameOverlayEvent.ElementType.FOOD
                    || event.getType() == RenderGameOverlayEvent.ElementType.AIR
                    || event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE
            ) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void renderOverlayPost(RenderGameOverlayEvent.Post event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            if(player.isCreative() || player.isSpectator()) return;
            IPlayerCap cap = PlayerCapFactory.get(player);
            BoostStats stats = cap.getBoostStats();
            renderBoostStats(event.getWindow(), stats, ConfigImpl.client.specialHUDRender);
        }
    }

    private static void renderBoostStats(MainWindow window, BoostStats stats, boolean specialRenderer) {
        int level = stats.getValue();
        float f = stats.getSaturation();
        if(specialRenderer) {
            int left = window.getScaledWidth() / 2 - 91;
            int top = window.getScaledHeight() - 35;
            int width = 183 ;
            RenderHelper.x16Blit(left, top, left + width, top + 10, 0, 0, 10, 1);
            float healthMod = Minecraft.getInstance().player.getHealth() / 20f;
            RenderHelper.drawColoredShape(left, top, (int)(left + width * healthMod), top + 10, 1.0F, healthMod, healthMod, 1.0F);
            RenderHelper.drawColoredShape(left, top - 20, left + width, top, 0.0F, 0.0F, 0.0F, 0.5F);
            float step = 1 / 20.0F * width;
            float boostBarWidth = step * (level + f);
            RenderHelper.drawColoredShape(left, top - 3, (int)(left + boostBarWidth), top - 1, 1.0F, 1.0F, 0.0F, 1.0F);
        } else {
            Minecraft mc = Minecraft.getInstance();
            int left = window.getScaledWidth() / 2 - 91;
            int top = window.getScaledHeight() - 35;
            FontRenderer renderer = mc.fontRenderer;
            if(level == 0 && f == 0f) return;
            String text = (int)((1f * f + level) / 20F * 100) + "%";
            renderer.drawStringWithShadow(text, left + 183, top + 5, 0xffffff);
        }
    }
}
