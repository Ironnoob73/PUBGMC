package dev.toma.pubgmc.client;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.InventoryFactory;
import dev.toma.pubgmc.capability.PMCInventoryHandler;
import dev.toma.pubgmc.capability.player.BoostStats;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.animation.HandAnimate;
import dev.toma.pubgmc.client.animation.types.SprintAnimation;
import dev.toma.pubgmc.client.render.OverlayGameRenderer;
import dev.toma.pubgmc.common.entity.IControllableEntity;
import dev.toma.pubgmc.common.inventory.SlotType;
import dev.toma.pubgmc.common.item.gun.Firemode;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.utility.BackpackSlotItem;
import dev.toma.pubgmc.common.item.wearable.IPMCArmor;
import dev.toma.pubgmc.config.Config;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.SPacketControllableInput;
import dev.toma.pubgmc.network.packet.SPacketOpenPlayerInventory;
import dev.toma.pubgmc.network.packet.SPacketSetAiming;
import dev.toma.pubgmc.network.packet.SPacketShoot;
import dev.toma.pubgmc.util.RenderHelper;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.GameSettings;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Pubgmc.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

    private static final Random rand = new Random();
    private static final StateListener runningAnimationFactory = new StateListener(PlayerEntity::isSprinting, player -> {
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof GunItem && Animations.SPRINTING.canPlay()) {
            AnimationManager.playNewAnimation(Animations.SPRINTING, new SprintAnimation());
        }
    });
    public static ScopeInfo scopeInfo;
    static boolean isShaderLoaded;

    @SubscribeEvent
    public static void cancelOverlayRenders(RenderGameOverlayEvent.Pre event) {
        if(Config.specialHUDRenderer.get()) {
            if(event.getType() == RenderGameOverlayEvent.ElementType.ARMOR || event.getType() == RenderGameOverlayEvent.ElementType.HEALTH || event.getType() == RenderGameOverlayEvent.ElementType.FOOD || event.getType() == RenderGameOverlayEvent.ElementType.AIR || event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
                event.setCanceled(true);
            } else if(event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof GunItem) {
                //event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void renderOverlayPost(RenderGameOverlayEvent.Post event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            IPlayerCap data = PlayerCapFactory.get(player);
            if(data.getAimInfo().isAiming() && scopeInfo != null) {
                MainWindow window = event.getWindow();
                int top = 25;
                int size = window.getScaledHeight() - 50;
                double left = window.getScaledWidth() / 2.0D - size / 2.0D;
                if(scopeInfo.shouldRenderPiP()) {
                    ClientManager.setScopeRendering(true);
                    GameRenderer renderer = mc.gameRenderer;
                    FogRenderer fogRenderer = renderer.fogRenderer;
                    // black frame background
                    RenderHelper.drawColoredShape(left - 2, top - 2, left + size + 2, top + size + 2, 0.0F, 0.0F, 0.0F, 1.0F);
                    // background with fog color
                    RenderHelper.drawColoredShape(left, top, left + size, top + size, fogRenderer.red, fogRenderer.green, fogRenderer.blue, 1.0F);
                    // bind texture from the other framebuffer
                    ClientManager.getFramebuffer().bindFramebufferTexture();
                    // render framebuffer texture
                    renderBoundTexture(left, top, left + size, top + size);
                }
                // scope overlay
                RenderHelper.drawTexturedShape(left, top, left + size, top + size, scopeInfo.getTextureOverlay());
            }

            if(player.getRidingEntity() instanceof IControllableEntity) {
                ((IControllableEntity) player.getRidingEntity()).drawOnScreen(mc, event.getWindow());
            }
            if(player.isSpectator()) return;
            IPlayerCap cap = PlayerCapFactory.get(player);
            BoostStats stats = cap.getBoostStats();
            renderHUD(mc, event.getWindow(), stats, Config.specialHUDRenderer.get());
        }
    }

    @SubscribeEvent
    public static void handleMouseInput(InputEvent.MouseInputEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        GameSettings settings = Minecraft.getInstance().gameSettings;
        if(player != null && event.getAction() == 1) {
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof GunItem) {
                GunItem gun = (GunItem) stack.getItem();
                Firemode firemode = gun.getFiremode(stack);
                CooldownTracker tracker = player.getCooldownTracker();
                IPlayerCap cap = PlayerCapFactory.get(player);
                if(settings.keyBindAttack.isPressed() && firemode == Firemode.SINGLE && !tracker.hasCooldown(gun) && !player.isSprinting() && !cap.getReloadInfo().isReloading()) {
                    shoot(gun, stack, player);
                } else if(settings.keyBindUseItem.isPressed() && Animations.AIMING.canPlay()) {
                    boolean aiming = !PlayerCapFactory.get(player).getAimInfo().isActualAim();
                    NetworkManager.sendToServer(new SPacketSetAiming(aiming, 0.2F * gun.getAttachment(AttachmentCategory.STOCK, stack).getAimSpeedMultiplier()));
                    ClientManager.needsShaderUpdate = true;
                    if(aiming) {
                        AnimationManager.playNewAnimation(Animations.AIMING, gun.getAnimations().getAimingAnimation(gun, stack));
                        scopeInfo = gun.getScopeData(stack);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(event.phase == TickEvent.Phase.START) return;
        if(player != null) {
            AnimationManager.clientTick();
            runningAnimationFactory.update(player);
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
            if(stack.getItem() instanceof GunItem) {
                GunItem gun = (GunItem) stack.getItem();
                CooldownTracker tracker = player.getCooldownTracker();
                if(settings.keyBindAttack.isKeyDown() && !tracker.hasCooldown(stack.getItem()) && gun.getFiremode(stack) == Firemode.FULL_AUTO && !player.isSprinting()) {
                    shoot(gun, stack, player);
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
        Minecraft mc = Minecraft.getInstance();
        if(event.phase == TickEvent.Phase.START) {
            if(ClientManager.shouldRenderScopeOverlay) {
                ClientManager.updateFramebufferSize(mc.mainWindow);
                Framebuffer main = mc.getFramebuffer();
                ClientManager.isRenderingPiP = true;
                Framebuffer overlay = ClientManager.getFramebuffer();
                overlay.bindFramebuffer(true);
                double backup = mc.gameSettings.fov;
                // fixes leaves render
                LeavesBlock.setRenderTranslucent(true);
                mc.gameSettings.fancyGraphics = true;
                mc.gameSettings.fov = scopeInfo.getZoom();
                GlStateManager.enableDepthTest();
                GlStateManager.enableAlphaTest();
                GlStateManager.alphaFunc(516, 0.5F);
                OverlayGameRenderer.updateCameraAndRender(mc.gameRenderer, event.renderTickTime, Util.nanoTime());
                mc.gameSettings.fov = backup;
                main.bindFramebuffer(true);
                ClientManager.isRenderingPiP = false;
                ClientManager.setScopeRendering(false);
            }
        } else {
            if(mc.player == null) return;
            boolean aiming = PlayerCapFactory.get(mc.player).getAimInfo().isAiming();
            if(aiming && scopeInfo != null && scopeInfo.shouldRenderPiP()) {
                if(!isShaderLoaded) {
                    isShaderLoaded = true;
                    mc.gameRenderer.loadShader(scopeInfo.getBlurShader());
                }
            } else if(!aiming && isShaderLoaded) {
                isShaderLoaded = false;
                mc.gameRenderer.stopUseShader();
            }
        }
        AnimationManager.renderTick(event.renderTickTime, event.phase);
    }

    @SubscribeEvent
    public static void openGui(GuiOpenEvent event) {
        if(event.getGui() instanceof InventoryScreen) {
            NetworkManager.sendToServer(new SPacketOpenPlayerInventory());
            event.setCanceled(true);
        } else if(event.getGui() instanceof MainMenuScreen) {
            event.setGui(new dev.toma.pubgmc.client.screen.menu.MainMenuScreen());
        }
    }

    private static void shoot(GunItem item, ItemStack stack, PlayerEntity player) {
        if(item.getAmmo(stack) > 0) {
            float vertical = item.getVerticalRecoil(player, stack);
            float horizontalUnmodified = item.getHorizontalRecoil(stack);
            float horizontal = rand.nextBoolean() ? horizontalUnmodified : -horizontalUnmodified;
            player.rotationPitch -= vertical;
            player.rotationYaw += horizontal;
            NetworkManager.sendToServer(new SPacketShoot());
        }
    }

    private static void renderHUD(Minecraft mc, MainWindow window, BoostStats stats, boolean specialRenderer) {
        int level = stats.getValue();
        float f = stats.getSaturation();
        PlayerEntity player = mc.player;
        int left = window.getScaledWidth() / 2 - 91;
        int top = window.getScaledHeight() - 35;
        if(specialRenderer) {
            boolean creative = player.isCreative();
            if(creative) {
                top += 13;
            }
            int width = 182;
            // health background
            if(!creative) {
                RenderHelper.x16Blit(left, top, left + width, top + 10, 0, 0, 10, 1);
                float healthMod = player.getHealth() / 20f;
                // health bar
                RenderHelper.drawColoredShape(left, top, (int)(left + width * healthMod), top + 10, 1.0F, healthMod, healthMod, 1.0F);
            }
            // black transparent bar
            RenderHelper.drawColoredShape(left, top - 20, left + width, top, 0.0F, 0.0F, 0.0F, 0.5F);
            float step = 1 / 20.0F * width;
            float boostBarWidth = step * (level + f);
            // boost bar
            RenderHelper.drawColoredShape(left, top - 3, (int)(left + boostBarWidth), top - 1, 1.0F, 1.0F, 0.0F, 1.0F);
            // armor icons
            ItemStack helmet = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
            ItemStack vest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
            if(helmet.getItem() instanceof IPMCArmor) {
                ((IPMCArmor) helmet.getItem()).renderIcon(left, top - 18, left + 16, top - 2, helmet);
            }
            if(vest.getItem() instanceof IPMCArmor) {
                ((IPMCArmor) vest.getItem()).renderIcon(left + 18, top - 18, left + 34, top - 2, vest);
            }
            PMCInventoryHandler handler = InventoryFactory.getInventoryHandler(player);
            ItemStack backpackStack = handler.getStackInSlot(SlotType.BACKPACK.ordinal());
            if(!backpackStack.isEmpty() && backpackStack.getItem() instanceof BackpackSlotItem) {
                int id = 1 + ((BackpackSlotItem) backpackStack.getItem()).getType().ordinal();
                RenderHelper.x32Blit(left + 36, top - 18, left + 52, top - 2, id, 2, 1, 1);
            }
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof GunItem) {
                GunItem gun = (GunItem) stack.getItem();
                int ammo = gun.getAmmo(stack);
                int total = UsefulFunctions.totalItemCountInInventory(gun.getAmmoType().getAmmo(), player.inventory);
                FontRenderer renderer = mc.fontRenderer;
                String info = ammo + "/" + total;
                renderer.drawStringWithShadow(info, left + 100 - renderer.getStringWidth(info) / 2.0F, top - 14, 0xFFFFFF);
                int firemode = gun.getFiremode(stack).ordinal();
                RenderHelper.x32Blit(left + 62, top - 18, left + 78, top - 2, 0, 2 + firemode, 1, 1);
            }

        } else {
            FontRenderer renderer = mc.fontRenderer;
            if(level == 0 && f == 0f) return;
            String text = (int)((1f * f + level) / 20F * 100) + "%";
            renderer.drawStringWithShadow(text, left + 183, top + 5, 0xffffff);
        }
    }

    private static void renderBoundTexture(double x1, double y1, double x2, double y2) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x1, y2, 0).tex(0.35, 0.3).endVertex();
        builder.pos(x2, y2, 0).tex(0.65, 0.3).endVertex();
        builder.pos(x2, y1, 0).tex(0.65, 0.7).endVertex();
        builder.pos(x1, y1, 0).tex(0.35, 0.7).endVertex();
        tessellator.draw();
    }

    private static class StateListener {

        private boolean lastState;
        private final Function<PlayerEntity, Boolean> stateGetter;
        private final Consumer<PlayerEntity> onStateChange;

        private StateListener(Function<PlayerEntity, Boolean> stateGetter, Consumer<PlayerEntity> onStateChange) {
            this.stateGetter = stateGetter;
            this.onStateChange = onStateChange;
        }

        private void update(PlayerEntity player) {
            boolean actual = stateGetter.apply(player);
            if(!lastState && actual) {
                onStateChange.accept(player);
            }
            lastState = actual;
        }
    }
}
