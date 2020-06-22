package dev.toma.pubgmc.client;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.api.IPMCArmor;
import dev.toma.pubgmc.api.entity.IControllableEntity;
import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.BoostStats;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.HandAnimate;
import dev.toma.pubgmc.common.item.gun.TestGun;
import dev.toma.pubgmc.config.Config;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.SPacketControllableInput;
import dev.toma.pubgmc.network.packet.SPacketShoot;
import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.GameSettings;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Pubgmc.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void cancelOverlayRenders(RenderGameOverlayEvent.Pre event) {
        if(Config.specialHUDRenderer.get()) {
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
            if(player.getRidingEntity() instanceof IControllableEntity) {
                ((IControllableEntity) player.getRidingEntity()).drawOnScreen(mc, event.getWindow());
            }
            if(player.isCreative() || player.isSpectator()) return;
            IPlayerCap cap = PlayerCapFactory.get(player);
            BoostStats stats = cap.getBoostStats();
            renderHUD(mc, event.getWindow(), stats, Config.specialHUDRenderer.get());
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(event.phase == TickEvent.Phase.START) return;
        if(player != null) {
            AnimationManager.clientTick();
            GameSettings settings = mc.gameSettings;
            Entity entity = player.getRidingEntity();
            if(entity instanceof IControllableEntity && entity.getControllingPassenger() == player) {
                IControllableEntity controllableEntity = (IControllableEntity) player.getRidingEntity();
                boolean fwd = settings.keyBindForward.isKeyDown();
                boolean bwd = settings.keyBindBack.isKeyDown();
                boolean right = settings.keyBindRight.isKeyDown();
                boolean left = settings.keyBindLeft.isKeyDown();
                boolean ptUp = settings.keyBindJump.isKeyDown();
                boolean ptDown = settings.keyBindSprint.isKeyDown();
                controllableEntity.onInputUpdate(fwd, bwd, right, left, ptUp, ptDown);
                NetworkManager.sendToServer(new SPacketControllableInput(fwd, bwd, right, left, ptUp, ptDown));
            }

            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof TestGun) {
                CooldownTracker tracker = player.getCooldownTracker();
                if(settings.keyBindAttack.isKeyDown() && !tracker.hasCooldown(stack.getItem())) {
                    NetworkManager.sendToServer(new SPacketShoot());
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderHandEvent(RenderSpecificHandEvent event) {
        ItemStack stack = event.getItemStack();
        ClientPlayerEntity player = Minecraft.getInstance().player;
        float partial = event.getPartialTicks();
        if(stack.getItem() instanceof HandAnimate) {
            HandAnimate animate = (HandAnimate) stack.getItem();
            event.setCanceled(true);
            float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partial;
            float swing = event.getSwingProgress();
            float equip = event.getEquipProgress();
            GlStateManager.pushMatrix();
            {
                AnimationManager.animateItemAndHands(partial);
                GlStateManager.pushMatrix();
                {
                    float yOff = -0.5F * equip;
                    AnimationManager.animateHands(partial);
                    GlStateManager.pushMatrix();
                    {
                        GlStateManager.disableCull();
                        GlStateManager.translatef(0, yOff, 0);
                        GlStateManager.pushMatrix();
                        {
                            AnimationManager.animateRightHand(partial);
                            animate.renderRightArm();
                        }
                        GlStateManager.popMatrix();
                        GlStateManager.pushMatrix();
                        {
                            AnimationManager.animateLeftHand(partial);
                            animate.renderLeftArm();
                        }
                        GlStateManager.popMatrix();
                        GlStateManager.enableCull();
                    }
                    GlStateManager.popMatrix();
                }
                GlStateManager.popMatrix();
            }
            AnimationManager.animateItem(partial);
            if(!AnimationManager.isItemRenderCanceled()) {
                Minecraft.getInstance().getFirstPersonRenderer().renderItemInFirstPerson(player, partial, pitch, Hand.MAIN_HAND, swing, stack, equip);
            }
            GlStateManager.popMatrix();
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        AnimationManager.renderTick(event.renderTickTime, event.phase);
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
