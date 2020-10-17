package dev.toma.pubgmc.client.animation.builder;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.animation.HandAnimate;
import dev.toma.pubgmc.client.screen.util.AttachmentSetupScreen;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.util.RenderHelper;
import dev.toma.pubgmc.util.object.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;
import java.util.Map;

public class BuilderMain {

    public static KeyBinding openToolUI;
    public static KeyBinding modelSetupUI;
    public static KeyBinding add;
    public static KeyBinding subtract;
    public static KeyBinding reset;
    public static KeyBinding activate;

    public static void init() {
        openToolUI = register("open_builder_ui", 77);
        modelSetupUI = register("model_setup", GLFW.GLFW_KEY_O);
        add = register("add", 265);
        subtract = register("subtract", 264);
        reset = register("reset", 88);
        activate = register("activate", 78);
        MinecraftForge.EVENT_BUS.addListener(BuilderMain::handleKeyInput);
        MinecraftForge.EVENT_BUS.addListener(BuilderMain::renderInfoOnScreen);
    }

    public static void handleKeyInput(InputEvent.KeyInputEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (openToolUI.isPressed()) {
            Minecraft.getInstance().displayGuiScreen(new AnimationBuilderScreen());
        } else if (add.isPressed()) {
            BuilderData.add(player.isSneaking());
        } else if (subtract.isPressed()) {
            BuilderData.subtract(player.isSneaking());
        } else if (activate.isPressed()) {
            boolean b = AnimationManager.isAnimationActive(Animations.DEBUG);
            if (b) AnimationManager.stopAnimation(Animations.DEBUG);
            else {
                BuilderData.current.setAnimationProgress(1.0F);
                AnimationManager.playNewAnimation(Animations.DEBUG, BuilderData.current);
            }
        } else if (reset.isPressed()) {
            BuilderData.resetToDefaultState();
            AnimationManager.playNewAnimation(Animations.DEBUG, BuilderData.current);
        } else if(modelSetupUI.isPressed()) {
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof GunItem) {
                Minecraft.getInstance().displayGuiScreen(new AttachmentSetupScreen(stack));
            }
        }
    }

    public static void renderInfoOnScreen(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && Minecraft.getInstance().currentScreen == null && Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof HandAnimate) {
            FontRenderer renderer = Minecraft.getInstance().fontRenderer;
            BuilderAnimationStep step = BuilderData.current;
            int totalLength = BuilderData.animationLength;
            renderer.drawStringWithShadow("Animation render: " + (AnimationManager.isAnimationActive(Animations.DEBUG) ? TextFormatting.GREEN + "Active" : TextFormatting.RED + "Inactive"), 10, 10, 0xffffff);
            renderer.drawStringWithShadow("Animation steps: " + TextFormatting.YELLOW + BuilderData.steps.size(), 10, 20, 0xffffff);
            renderer.drawStringWithShadow(String.format("Animation length: %s ticks", TextFormatting.YELLOW.toString() + totalLength), 10, 30, 0xffffff);
            renderer.drawStringWithShadow("Context: " + TextFormatting.AQUA + BuilderData.context.name(), 10, 40, 0xffffff);
            renderer.drawStringWithShadow("Axis: " + TextFormatting.AQUA + BuilderData.axis.name(), 10, 50, 0xffffff);
            renderer.drawStringWithShadow("Part: " + TextFormatting.AQUA + BuilderData.part.name(), 10, 60, 0xffffff);
            renderer.drawStringWithShadow(TextFormatting.BOLD + "Current animation", 10, 75, 0xffffff);
            renderer.drawStringWithShadow("Length: " + TextFormatting.YELLOW.toString() + (int) (totalLength * step.length), 10, 85, 0xffffff);
            int j = 0;
            DecimalFormat df = new DecimalFormat("###.##");
            for (BuilderData.Part part : BuilderData.Part.values()) {
                BuilderAnimationStep.Data data = step.map.get(part);
                if (data.isEmpty()) continue;
                renderer.drawStringWithShadow(TextFormatting.UNDERLINE + part.name(), 10, 95 + j * 10, 0xffffff);
                BuilderAnimationStep.TranslationContext ctx0 = data.translationContext;
                if (!ctx0.isEmpty()) {
                    ++j;
                    renderer.drawStringWithShadow(String.format(TextFormatting.YELLOW + "Move" + TextFormatting.WHITE + ":[%s+%s, %s+%s, %s+%s]", TextFormatting.RED + df.format(ctx0.baseX) + TextFormatting.WHITE, TextFormatting.GREEN + df.format(ctx0.newX) + TextFormatting.WHITE, TextFormatting.RED + df.format(ctx0.baseY) + TextFormatting.WHITE, TextFormatting.GREEN + df.format(ctx0.newY) + TextFormatting.WHITE, TextFormatting.RED + df.format(ctx0.baseZ) + TextFormatting.WHITE, TextFormatting.GREEN + df.format(ctx0.newZ) + TextFormatting.WHITE), 10, 95 + j * 10, 0xffffff);
                }
                BuilderAnimationStep.RotationContext ctx = data.rotationContext;
                if (!ctx.isEmpty()) {
                    ++j;
                    StringBuilder builder = new StringBuilder();
                    builder.append(TextFormatting.YELLOW).append("Rotate").append(TextFormatting.WHITE).append(":[");
                    for (Map.Entry<BuilderData.Axis, Pair<Float, Float>> entry : ctx.rotations.entrySet()) {
                        Pair<Float, Float> pair = entry.getValue();
                        builder.append(TextFormatting.AQUA).append(entry.getKey().name()).append(TextFormatting.WHITE).append(":").append(TextFormatting.RED).append(df.format(pair.getLeft())).append(TextFormatting.WHITE).append("+").append(TextFormatting.GREEN).append(df.format(pair.getRight())).append(TextFormatting.WHITE).append(";");
                    }
                    builder.append("]");
                    renderer.drawStringWithShadow(builder.toString(), 10, 95 + j * 10, 0xffffff);
                }
                ++j;
            }
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0, 0, -1);
            RenderHelper.drawColoredShape(0, 0, 170, 95 + j * 10 + 10, 0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.popMatrix();
        }
    }

    private static KeyBinding register(String name, int key) {
        KeyBinding binding = new KeyBinding("pubgmc.key." + name, key, "pubgmc.key.category");
        ClientRegistry.registerKeyBinding(binding);
        return binding;
    }
}
