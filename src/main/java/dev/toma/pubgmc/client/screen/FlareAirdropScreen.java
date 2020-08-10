package dev.toma.pubgmc.client.screen;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.FlareAirdropContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FlareAirdropScreen extends ContainerScreen<FlareAirdropContainer> {

    private static final ResourceLocation RL = Pubgmc.makeResource("textures/screen/flare_airdrop.png");

    public FlareAirdropScreen(FlareAirdropContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(RL);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
}
