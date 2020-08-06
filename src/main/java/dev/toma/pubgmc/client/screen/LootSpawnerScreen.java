package dev.toma.pubgmc.client.screen;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.LootSpawnerContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class LootSpawnerScreen extends ContainerScreen<LootSpawnerContainer> {

    private static final ResourceLocation TEXTURE = Pubgmc.makeResource("textures/screen/loot_spawner.png");

    public LootSpawnerScreen(LootSpawnerContainer container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        renderHoveredToolTip(p_render_1_, p_render_2_);
    }
}
