package dev.toma.pubgmc.client.animation.builder;

import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.model.gun.AbstractGunModel;
import dev.toma.pubgmc.client.render.item.GunRenderer;
import dev.toma.pubgmc.util.RenderHelper;
import dev.toma.pubgmc.util.object.Optional;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnimationBuilderScreen extends Screen {

    private int center;
    private int index = 0;
    private int entryCount;
    public final Optional<InputComponent> selectedInput = Optional.empty();
    private final List<UIComponent> componentList = new ArrayList<>();

    public AnimationBuilderScreen() {
        super(new StringTextComponent("no title"));
    }

    @Override
    public void init() {
        componentList.clear();
        center = width / 2;
        entryCount = (height - 70) / 35;
        if(entryCount == 0) {
            addComponent(new PlainTextComponent(10, 70, TextFormatting.RED + "Screen is too small to display animation data"));
        }
        addComponent(new PlainTextComponent(10, 3, TextFormatting.BOLD + "General"));
        addComponent(new PressableComponent(10, 20, 75, 20, "Play Animation", ac -> AnimationManager.playNewAnimation(Animations.DEBUG, BuilderData.asAnimation())));
        addComponent(new PressableComponent(95, 20, 75, 20, "Export as .txt", ac -> BuiltAnimationExporter.exportAnimation()));
        addComponent(new UIComponent(175, 20, 40, 20, "Gun", ac -> {
            BuilderData.buildingGunAnimation = !BuilderData.buildingGunAnimation;
            init();
        }).setState(BuilderData.buildingGunAnimation));
        addComponent(new PlainTextComponent(10, 45, "Animation length:"));
        PlainTextComponent textComponent = new PlainTextComponent(95, 45, TextFormatting.BOLD.toString() + BuilderData.animationLength + TextFormatting.RESET + " ticks");
        addComponent(new PressableComponent(145, 45, 20, 20, "-", ac -> {
            BuilderData.animationLength = Math.max(1, hasShiftDown() ? BuilderData.animationLength - 10 : BuilderData.animationLength - 1);
            textComponent.setName(TextFormatting.BOLD.toString() + BuilderData.animationLength + TextFormatting.RESET + " ticks");
        }));
        addComponent(new PressableComponent(170, 45, 20, 20, "+", ac -> {
            BuilderData.animationLength = hasShiftDown() ? BuilderData.animationLength + 10 : BuilderData.animationLength + 1;
            textComponent.setName(TextFormatting.BOLD.toString() + BuilderData.animationLength + TextFormatting.RESET + " ticks");
        }));
        addComponent(textComponent);
        for(int i = index; i < index + entryCount; i++) {
            int id = i - index;
            if(i >= BuilderData.steps.size()) break;
            BuilderAnimationStep step = BuilderData.steps.get(i);
            addComponent(new AnimationStepComponent(this, i, 10, 70 + id * 35, center - 30, 30, step));
        }
        addComponent(new PlainTextComponent(center + 10, 3, TextFormatting.BOLD + "Animation control"));
        addComponent(new PlainTextComponent(center + 10, 20, "Context"));
        addComponent(new UIComponent(center + 70, 20, 80, 20, "Translate", ac -> {
            BuilderData.context = BuilderData.Context.TRANSLATION;
            init();
        }).setState(BuilderData.context == BuilderData.Context.TRANSLATION));
        addComponent(new UIComponent(center + 160, 20, 80, 20, "Rotate", ac -> {
            BuilderData.context = BuilderData.Context.ROTATION;
            init();
        }).setState(BuilderData.context == BuilderData.Context.ROTATION));
        addComponent(new PlainTextComponent(center + 10, 45, "Axis"));
        addComponent(new UIComponent(center + 40, 45, 40, 20, "X", ac -> {
            BuilderData.axis = BuilderData.Axis.X;
            init();
        }).setState(BuilderData.axis == BuilderData.Axis.X));
        addComponent(new UIComponent(center + 90, 45, 40, 20, "Y", ac -> {
            BuilderData.axis = BuilderData.Axis.Y;
            init();
        }).setState(BuilderData.axis == BuilderData.Axis.Y));
        addComponent(new UIComponent(center + 140, 45, 40, 20, "Z", ac -> {
            BuilderData.axis = BuilderData.Axis.Z;
            init();
        }).setState(BuilderData.axis == BuilderData.Axis.Z));
        addComponent(new PlainTextComponent(center + 10, 70, "Part"));
        addComponent(new UIComponent(center + 40, 70, 80, 20, "Item and hands", ac -> {
            BuilderData.part = BuilderData.Part.ITEM_AND_HANDS;
            init();
        }).setState(BuilderData.part == BuilderData.Part.ITEM_AND_HANDS));
        addComponent(new UIComponent(center + 40, 95, 80, 20, "Hands", ac -> {
            BuilderData.part = BuilderData.Part.HANDS;
            init();
        }).setState(BuilderData.part == BuilderData.Part.HANDS));
        addComponent(new UIComponent(center + 40, 120, 80, 20, "Right hand", ac -> {
            BuilderData.part = BuilderData.Part.RIGHT_HAND;
            init();
        }).setState(BuilderData.part == BuilderData.Part.RIGHT_HAND));
        addComponent(new UIComponent(center + 40, 145, 80, 20, "Left hand", ac -> {
            BuilderData.part = BuilderData.Part.LEFT_HAND;
            init();
        }).setState(BuilderData.part == BuilderData.Part.LEFT_HAND));
        addComponent(new UIComponent(center + 40, 170, 80, 20, "Item", ac -> {
            BuilderData.part = BuilderData.Part.ITEM;
            init();
        }).setState(BuilderData.part == BuilderData.Part.ITEM));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void addComponent(UIComponent component) {
        componentList.add(component);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderHelper.drawColoredShape(0, 0, width, height, 0.0F, 0.0F, 0.0F, 0.4F);
        RenderHelper.line(center, 0, center, height, 1.0F, 1.0F, 1.0F, 1.0F, 3);
        componentList.forEach(c -> c.draw(minecraft.fontRenderer));
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        selectedInput.ifPresent(cp -> cp.keyPressed(typedChar, keyCode));
        return super.charTyped(typedChar, keyCode);
    }

    @Override
    public boolean keyPressed(int k1, int k2, int k3) {
        if(k1 == 259) {
            selectedInput.ifPresent(cp -> cp.keyPressed('\b', 259));
        }
        return super.keyPressed(k1, k2, k3);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double value) {
        int i = -(int) value;
        int next = index + i;
        if(next < BuilderData.steps.size() - entryCount + 1 && next >= 0) {
            index = next;
            init();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if(mouseButton != 0) return false;
        selectedInput.clear();
        for (UIComponent component : componentList) {
            if (component.isMouseOver((int) mouseX, (int) mouseY)) {
                if (component instanceof InputComponent) {
                    InputComponent inputComponent = (InputComponent) component;
                    if (inputComponent == selectedInput.get()) selectedInput.clear();
                    else selectedInput.map(inputComponent);
                }
                component.onClick(mouseX, mouseY);
                minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.75F, 1.0F);
                break;
            }
        }
        return false;
    }
}
