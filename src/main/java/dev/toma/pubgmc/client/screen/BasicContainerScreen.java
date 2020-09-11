package dev.toma.pubgmc.client.screen;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class BasicContainerScreen<C extends Container> extends ContainerScreen<C> {

    public BasicContainerScreen(C container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
    }

    public abstract ResourceLocation getTexture();

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(this.getTexture());
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
}
