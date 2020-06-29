package dev.toma.pubgmc.client;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.animation.types.SprintAnimation;
import dev.toma.pubgmc.common.item.gun.Firemode;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.wearable.IPMCArmor;
import dev.toma.pubgmc.common.entity.IControllableEntity;
import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.BoostStats;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.HandAnimate;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.config.Config;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.SPacketControllableInput;
import dev.toma.pubgmc.network.packet.SPacketSetAiming;
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
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Pubgmc.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

    private static Random rand = new Random();
    private static final StateListener runningAnimationFactory = new StateListener(PlayerEntity::isSprinting, player -> {
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof GunItem && Animations.SPRINTING.canPlay()) {
            AnimationManager.playNewAnimation(Animations.SPRINTING, new SprintAnimation());
        }
    });

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
                    if(aiming) AnimationManager.playNewAnimation(Animations.AIMING, gun.getAimAnimation());
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
        AnimationManager.renderTick(event.renderTickTime, event.phase);
    }

    private static void shoot(GunItem item, ItemStack stack, PlayerEntity player) {
        float vertical = item.getVerticalRecoil(player, stack);
        float horizontalUnmodified = item.getHorizontalRecoil(stack);
        float horizontal = rand.nextBoolean() ? horizontalUnmodified : -horizontalUnmodified;
        player.rotationPitch -= vertical;
        player.rotationYaw += horizontal;
        NetworkManager.sendToServer(new SPacketShoot());
    }

    private static void renderHUD(Minecraft mc, MainWindow window, BoostStats stats, boolean specialRenderer) {
        int level = stats.getValue();
        float f = stats.getSaturation();
        PlayerEntity player = mc.player;
        int left = window.getScaledWidth() / 2 - 91;
        int top = window.getScaledHeight() - 35;
        if(specialRenderer) {
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
            // TODO gun icons

        } else {
            FontRenderer renderer = mc.fontRenderer;
            if(level == 0 && f == 0f) return;
            String text = (int)((1f * f + level) / 20F * 100) + "%";
            renderer.drawStringWithShadow(text, left + 183, top + 5, 0xffffff);
        }
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
