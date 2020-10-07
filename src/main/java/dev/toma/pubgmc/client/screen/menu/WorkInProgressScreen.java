package dev.toma.pubgmc.client.screen.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class WorkInProgressScreen extends Screen {

    private final Screen lastScreen;

    public WorkInProgressScreen(Screen currentScreen) {
        super(new StringTextComponent(""));
        this.lastScreen = currentScreen;
    }

    @Override
    protected void init() {
        int px = (width - 60) / 2;
        int py = (height - 20) / 2;
        this.addButton(new Button(px, py + 20, 60, 20, "Ok", b -> Minecraft.getInstance().displayGuiScreen(WorkInProgressScreen.this.lastScreen)));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        String text = "Work in progress... Coming soon";
        int tw = font.getStringWidth(text);
        font.drawStringWithShadow(text, (width - tw) / 2f, (height - font.FONT_HEIGHT) / 2f, 0xff0000);
        super.render(mouseX, mouseY, partialTicks);
    }
}
