package dev.toma.pubgmc.client.screen.component;

import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class ImagePressableComponent extends PressableComponent {

    final ResourceLocation texture;

    public ImagePressableComponent(int x, int y, int width, int height, ResourceLocation texture, Consumer<PressableComponent> consumer) {
        super(x, y, width, height, consumer);
        this.texture = texture;
    }

    @Override
    public void draw(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(texture);
        RenderHelper.drawColoredShape(x, y, x + width, y + height, 0.0F, 0.0F, 0.0F, 0.4F);
        RenderHelper.drawColoredTexturedShape(x + 2, y + 2, x + width - 2, y + height - 2, 1.0F, 1.0F, 1.0F, isEnabled() ? 1.0F : 0.4F);
        if(isEnabled() && isMouseOver(mouseX, mouseY)) {
            RenderHelper.drawColoredShape(x, y, x + width, y + height, 1.0F, 1.0F, 1.0F, 0.5F);
        }
    }
}
