package dev.toma.pubgmc.internal.animation;

import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.client.model.gun.AbstractGunModel;
import dev.toma.pubgmc.client.render.item.GunRenderer;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.internal.InternalData;
import dev.toma.pubgmc.util.RenderHelper;
import dev.toma.pubgmc.util.object.Optional;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.item.ItemStack;
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
        IAnimationPart.refresh();
        if(entryCount == 0) {
            addComponent(new PlainTextComponent(10, 70, TextFormatting.RED + "Screen is too small to display animation data"));
        }
        addComponent(new PlainTextComponent(10, 3, TextFormatting.BOLD + "General"));
        addComponent(new PressableComponent(10, 20, 75, 20, "Play Animation", ac -> AnimationManager.playNewAnimation(Animations.DEBUG, InternalData.asAnimation())));
        addComponent(new PressableComponent(95, 20, 75, 20, "Export as .txt", ac -> BuiltAnimationExporter.exportAnimation()));
        addComponent(new UIComponent(175, 20, 40, 20, "Gun", ac -> {
            InternalData.buildingGunAnimation = !InternalData.buildingGunAnimation;
            init();
        }).setState(InternalData.buildingGunAnimation));
        addComponent(new PlainTextComponent(10, 45, "Animation length:"));
        PlainTextComponent textComponent = new PlainTextComponent(95, 45, TextFormatting.BOLD.toString() + InternalData.animationLength + TextFormatting.RESET + " ticks");
        addComponent(new PressableComponent(145, 45, 20, 20, "-", ac -> {
            InternalData.animationLength = Math.max(1, hasShiftDown() ? InternalData.animationLength - 10 : InternalData.animationLength - 1);
            textComponent.setName(TextFormatting.BOLD.toString() + InternalData.animationLength + TextFormatting.RESET + " ticks");
        }));
        addComponent(new PressableComponent(170, 45, 20, 20, "+", ac -> {
            InternalData.animationLength = hasShiftDown() ? InternalData.animationLength + 10 : InternalData.animationLength + 1;
            textComponent.setName(TextFormatting.BOLD.toString() + InternalData.animationLength + TextFormatting.RESET + " ticks");
        }));
        addComponent(textComponent);
        for(int i = index; i < index + entryCount; i++) {
            int id = i - index;
            if(i >= InternalData.steps.size()) break;
            BuilderAnimationStep step = InternalData.steps.get(i);
            addComponent(new AnimationStepComponent(this, i, 10, 70 + id * 35, center - 30, 30, step));
        }
        addComponent(new PlainTextComponent(center + 10, 3, TextFormatting.BOLD + "Animation control"));
        addComponent(new PlainTextComponent(center + 10, 20, "Context"));
        addComponent(new UIComponent(center + 70, 20, 80, 20, "Translate", ac -> {
            InternalData.context = InternalData.Context.TRANSLATION;
            init();
        }).setState(InternalData.context == InternalData.Context.TRANSLATION));
        addComponent(new UIComponent(center + 160, 20, 80, 20, "Rotate", ac -> {
            InternalData.context = InternalData.Context.ROTATION;
            init();
        }).setState(InternalData.context == InternalData.Context.ROTATION));
        addComponent(new PlainTextComponent(center + 10, 45, "Axis"));
        addComponent(new UIComponent(center + 40, 45, 40, 20, "X", ac -> {
            InternalData.axis = InternalData.Axis.X;
            init();
        }).setState(InternalData.axis == InternalData.Axis.X));
        addComponent(new UIComponent(center + 90, 45, 40, 20, "Y", ac -> {
            InternalData.axis = InternalData.Axis.Y;
            init();
        }).setState(InternalData.axis == InternalData.Axis.Y));
        addComponent(new UIComponent(center + 140, 45, 40, 20, "Z", ac -> {
            InternalData.axis = InternalData.Axis.Z;
            init();
        }).setState(InternalData.axis == InternalData.Axis.Z));
        addComponent(new PlainTextComponent(center + 10, 70, "Part"));
        addComponent(new UIComponent(center + 40, 70, 80, 20, "Item and hands", ac -> {
            InternalData.part = IAnimationPart.ITEM_AND_HANDS;
            init();
        }).setState(InternalData.part == IAnimationPart.ITEM_AND_HANDS));
        addComponent(new UIComponent(center + 40, 95, 80, 20, "Hands", ac -> {
            InternalData.part = IAnimationPart.HANDS;
            init();
        }).setState(InternalData.part == IAnimationPart.HANDS));
        addComponent(new UIComponent(center + 40, 120, 80, 20, "Right hand", ac -> {
            InternalData.part = IAnimationPart.RIGHT_HAND;
            init();
        }).setState(InternalData.part == IAnimationPart.RIGHT_HAND));
        addComponent(new UIComponent(center + 40, 145, 80, 20, "Left hand", ac -> {
            InternalData.part = IAnimationPart.LEFT_HAND;
            init();
        }).setState(InternalData.part == IAnimationPart.LEFT_HAND));
        addComponent(new UIComponent(center + 40, 170, 80, 20, "Item", ac -> {
            InternalData.part = IAnimationPart.ITEM;
            init();
        }).setState(InternalData.part == IAnimationPart.ITEM));

        ItemStack stack = minecraft.player.getHeldItemMainhand();
        if(!stack.isEmpty() && stack.getItem() instanceof GunItem) {
            GunRenderer renderer = (GunRenderer) stack.getItem().getTileEntityItemStackRenderer();
            AbstractGunModel gunModel = renderer.getModel();
            Map<Integer, RendererModel> map = gunModel.getAnimatedPartMap();
            if(!map.isEmpty()) {
                int i = 0;
                for (int key : map.keySet()) {
                    addComponent(new UIComponent(center + 40, 195 + i * 25, 80, 20, "" + key, ac -> {
                        InternalData.part = new IAnimationPart.Model(key);
                        init();
                    }).setState(InternalData.part.equals(new IAnimationPart.Model(key).addToList())));
                    ++i;
                }
            }
        }
        if(InternalData.current.map.size() < IAnimationPart.PARTS.size()) {
            InternalData.steps = new ArrayList<>();
            InternalData.steps.add(new BuilderAnimationStep());
            InternalData.current = InternalData.steps.get(0);
        }
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
        if(next < InternalData.steps.size() - entryCount + 1 && next >= 0) {
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
