package dev.toma.pubgmc.client.screen.component;

import net.minecraft.client.Minecraft;

public class PlainTextComponent extends Component {

    private final String text;
    private final boolean shouldCenter;

    public PlainTextComponent(int x, int y, int width, int height, String text, boolean shouldCenter) {
        super(x, y, width, height);
        this.text = text;
        this.shouldCenter = shouldCenter;
    }

    @Override
    public void draw(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        int width = mc.fontRenderer.getStringWidth(text);
        if(shouldCenter) {
            mc.fontRenderer.drawString(text, x + (this.width - width) / 2f, y + (this.height - mc.fontRenderer.FONT_HEIGHT) / 2.0f, 0xffffff);
        } else {
            mc.fontRenderer.drawString(text, x, y + (this.height - mc.fontRenderer.FONT_HEIGHT) / 2.0F, 0xffffff);
        }
    }
}
