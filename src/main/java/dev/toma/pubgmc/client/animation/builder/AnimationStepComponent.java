package dev.toma.pubgmc.client.animation.builder;

import dev.toma.pubgmc.client.animation.Animation;
import dev.toma.pubgmc.client.animation.AnimationManager;
import dev.toma.pubgmc.client.animation.Animations;
import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class AnimationStepComponent extends UIComponent {

    private final int id;
    private final AnimationBuilderScreen screen;
    private boolean selected;

    public AnimationStepComponent(AnimationBuilderScreen screen, int ID, int x, int y, int width, int height, BuilderAnimationStep step) {
        super(x, y, width, height);
        this.screen = screen;
        this.id = ID;
        screen.addComponent(new PlainTextComponent(x + 5, y + 5, ID + ""));
        screen.addComponent(new InputComponent(screen, ID, x + 20, y + 5, 50, 20, step.getIntValue()));
        screen.addComponent(new PressableComponent(x + 75, y + 5, 40, 20, "Play", ac -> {
            Animation animation = step.asTempAnimation();
            if(animation == null) {
                Minecraft.getInstance().player.sendMessage(new StringTextComponent(TextFormatting.RED + "Unable to create animation!"));
                return;
            }
            AnimationManager.playNewAnimation(Animations.DEBUG, animation);
        }));
        screen.addComponent(new PressableComponent(x + 120, y + 5, 40, 20, "Add", ac -> {
            BuilderAnimationStep newStep = new BuilderAnimationStep(step);
            BuilderData.steps.add(ID + 1, newStep);
            BuilderData.current = newStep;
            screen.init();
        }));
        screen.addComponent(new PressableComponent(x + 165, y + 5, 40, 20, "Remove", ac -> {
            BuilderData.steps.remove(ID);
            if(BuilderData.steps.isEmpty()) {
                BuilderAnimationStep step1 = new BuilderAnimationStep();
                BuilderData.steps.add(step1);
                BuilderData.current = step1;
            } else if(BuilderData.current == null) {
                BuilderData.current = BuilderData.steps.get(BuilderData.steps.size() - 1);
            }
            screen.init();
        }));
        this.selected = BuilderData.current == step;
    }

    @Override
    public void onClick(double mousex, double mousey) {
        if(!selected) {
            if(id < BuilderData.steps.size()) {
                BuilderData.current = BuilderData.steps.get(id);
                screen.init();
            }
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void draw(FontRenderer renderer) {
        if(selected) {
            RenderHelper.line(x, y, x + w, y, 0.0F, 0.8F, 0.0F, 1.0F, 4);
            RenderHelper.line(x, y, x, y + h, 0.0F, 0.8F, 0.0F, 1.0F, 4);
            RenderHelper.line(x + w, y, x + w, y + h, 0.0F, 0.8F, 0.0F, 1.0F, 4);
            RenderHelper.line(x, y + h, x + w, y + h, 0.0F, 0.8F, 0.0F, 1.0F, 4);
        } else {
            RenderHelper.line(x, y, x + w, y, 1.0F, 1.0F, 1.0F, 1.0F, 4);
            RenderHelper.line(x, y, x, y + h, 1.0F, 1.0F, 1.0F, 1.0F, 4);
            RenderHelper.line(x + w, y, x + w, y + h, 1.0F, 1.0F, 1.0F, 1.0F, 4);
            RenderHelper.line(x, y + h, x + w, y + h, 1.0F, 1.0F, 1.0F, 1.0F, 4);
        }
    }
}
