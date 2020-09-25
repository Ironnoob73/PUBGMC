package dev.toma.pubgmc.client.screen.menu;

import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.Consumer;

public class SelectContentTypeScreen extends ComponentScreen {

    private final Screen lastScreen;

    public SelectContentTypeScreen(Screen lastScreen) {
        super(new StringTextComponent("Select content type screen"));
        this.lastScreen = lastScreen;
    }

    @Override
    public void initComponents() {
        CenteredTextButtonComponent content = new CenteredTextButtonComponent(0, 0, width / 2, height - 50, "Community Content", pc -> {});
        content.setEnabled(false);
        addComponent(content);
        addComponent(new CenteredTextButtonComponent(width/2, 0, width/2, height-50, "My Worlds", pc -> minecraft.displayGuiScreen(new WorldSelectionScreen(this))));
        addComponent(new CenteredTextButtonComponent(0, height - 50, width, 50, "Back to menu", pc -> minecraft.displayGuiScreen(lastScreen)));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        minecraft.getTextureManager().bindTexture(MainMenuScreen.BACKGROUND_TEXTURE);
        RenderHelper.drawTexturedShape(0, 0, width, height);
        super.render(mouseX, mouseY, partialTicks);
    }

    static class CenteredTextButtonComponent extends ButtonComponent {

        public CenteredTextButtonComponent(int x, int y, int width, int height, String text, Consumer<PressableComponent> consumer) {
            super(x, y, width, height, text, consumer);
        }

        @Override
        public void draw(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            boolean hovered = isMouseOver(mouseX, mouseY);
            RenderHelper.drawColoredShape(x, y, x + width, y + height, 0.0F, 0.0F, 0.0F, 1.0F);
            float color = hovered && isEnabled() ? 0.6F : 0.4F;
            RenderHelper.drawColoredShape(x + 2, y + 2, x + width - 2, y + height - 2, color, color, color, 1.0F);
            RenderHelper.drawColoredShape(x + 4, y + 4, x + width - 2, y + height - 2, color - 0.2F, color - 0.2F, color - 0.2F, 1.0F);
            RenderHelper.drawColoredShape(x + 4, y + 4, x + width - 4, y + height - 4, color - 0.1F, color - 0.1F, color - 0.1F, 1.0F);
            if(!isEnabled()) {
                RenderHelper.drawColoredShape(x + 2, y + 2, x + width - 2, y + height - 2, 0.0F, 0.0F, 0.0F, 0.5F);
            }
            int w = mc.fontRenderer.getStringWidth(displayString);
            mc.fontRenderer.drawStringWithShadow(displayString, x + (width - w) / 2.0f, y + (height - mc.fontRenderer.FONT_HEIGHT) / 2.0f, isEnabled() ? hovered ? 0xffff00 : 0xffffff : 0x993333);
        }
    }
}
