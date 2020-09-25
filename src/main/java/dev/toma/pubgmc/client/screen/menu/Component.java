package dev.toma.pubgmc.client.screen.menu;

import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.Minecraft;

public class Component {

    public int x;
    public int y;
    public int width;
    public int height;

    public Component(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        RenderHelper.drawColoredShape(x, y, x + width, y + height, 0.0F, 0.0F, 0.0F, 1.0F);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void handleClicked(double mouseX, double mouseY, int mouseButton) {

    }

    public void handleScrolled(double mouseX, double mouseY, double amount) {

    }

    public boolean allowUnhoveredScrolling() {
        return false;
    }

    protected interface Tickable {

        void tickComponent();
    }
}
