package dev.toma.pubgmc.client.screen.menu;

import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

public class ButtonComponent extends PressableComponent {

    protected final String displayString;

    public ButtonComponent(int x, int y, int width, int height, String text, Consumer<PressableComponent> consumer) {
        super(x, y, width, height, consumer);
        this.displayString = text;
    }

    @Override
    public void draw(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        boolean hovered = isMouseOver(mouseX, mouseY);
        RenderHelper.drawColoredShape(x, y, x + width, y + height, 0.0F, 0.0F, 0.0F, 0.5F);
        if(hovered && isEnabled()) {
            RenderHelper.drawColoredShape(x, y, x + width, y + height, 1.0F, 1.0F, 1.0F, 0.5F);
        }
        int w = mc.fontRenderer.getStringWidth(displayString);
        mc.fontRenderer.drawStringWithShadow(displayString, x + (width - w) / 2.0f, y + (height - mc.fontRenderer.FONT_HEIGHT) / 2.0f, hovered ? 0xffff00 : 0xffffff);
    }
}
