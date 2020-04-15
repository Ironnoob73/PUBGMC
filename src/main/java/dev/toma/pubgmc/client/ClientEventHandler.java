package dev.toma.pubgmc.client;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.api.IPMCArmor;
import dev.toma.pubgmc.api.entity.IControllableEntity;
import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.BoostStats;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.config.ConfigImpl;
import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.GameSettings;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
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
            renderHUD(mc, event.getWindow(), stats, ConfigImpl.client.specialHUDRender);
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player != null) {
            GameSettings settings = mc.gameSettings;
            Entity entity = player.getRidingEntity();
            if(entity instanceof IControllableEntity && entity.getControllingPassenger() == player) {
                IControllableEntity controllableEntity = (IControllableEntity) player.getRidingEntity();
                boolean fwd = settings.keyBindForward.isKeyDown();
                boolean bwd = settings.keyBindBack.isKeyDown();
                boolean right = settings.keyBindRight.isKeyDown();
                boolean left = settings.keyBindLeft.isKeyDown();
                controllableEntity.onInputUpdate(fwd, bwd, right, left);
            }
        }
    }

    private static void renderHUD(Minecraft mc, MainWindow window, BoostStats stats, boolean specialRenderer) {
        int level = stats.getValue();
        float f = stats.getSaturation();
        PlayerEntity player = mc.player;
        if(specialRenderer) {
            int left = window.getScaledWidth() / 2 - 91;
            int top = window.getScaledHeight() - 35;
            int width = 183 ;
            // health background
            RenderHelper.x16Blit(left, top, left + width, top + 10, 0, 0, 10, 1);
            float healthMod = player.getHealth() / 20f;
            // health bar
            RenderHelper.drawColoredShape(left, top, (int)(left + width * healthMod), top + 10, 1.0F, healthMod, healthMod, 1.0F);
            // black transparent bar
            RenderHelper.drawColoredShape(left, top - 20, left + width, top, 0.0F, 0.0F, 0.0F, 0.5F);
            float step = 1 / 20.0F * width;
            float boostBarWidth = step * (level + f);
            // boost bar
            RenderHelper.drawColoredShape(left, top - 3, (int)(left + boostBarWidth), top - 1, 1.0F, 1.0F, 0.0F, 1.0F);
            // armor icons
            ItemStack helmet = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
            ItemStack vest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
            int id = 0;
            if(helmet.getItem() instanceof IPMCArmor) {
                ((IPMCArmor) helmet.getItem()).renderIcon(left + id * 18, top - 20, left + id * 18 + 16, top - 4, helmet);
                ++id;
            }
            if(vest.getItem() instanceof IPMCArmor) {
                ((IPMCArmor) vest.getItem()).renderIcon(left + id * 18, top - 20, left + id * 18 + 16, top - 4, vest);
                ++id;
            }
            // TODO backpack if custom inventory is enabled

        } else {
            int left = window.getScaledWidth() / 2 - 91;
            int top = window.getScaledHeight() - 35;
            FontRenderer renderer = mc.fontRenderer;
            if(level == 0 && f == 0f) return;
            String text = (int)((1f * f + level) / 20F * 100) + "%";
            renderer.drawStringWithShadow(text, left + 183, top + 5, 0xffffff);
        }
    }
}
