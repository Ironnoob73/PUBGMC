package dev.toma.pubgmc.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.AttachmentContainer;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
import dev.toma.pubgmc.common.item.gun.attachment.GunAttachments;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class AttachmentScreen extends ContainerScreen<AttachmentContainer> {

    static final ResourceLocation BG = Pubgmc.makeResource("textures/screen/attachments.png");
    private final ItemStack displayStack;

    public AttachmentScreen(AttachmentContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
        ySize = 197;
        this.displayStack = container.getStack().copy();
    }

    @Override
    protected void init() {
        super.init();
        if(!(displayStack.getItem() instanceof GunItem)) {
            minecraft.displayGuiScreen(null);
            minecraft.player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Couldn't open attachment menu for " + displayStack.getItem().getClass().getSimpleName()), true);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(BG);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
        ItemStack dragging = minecraft.player.inventory.getItemStack();
        if(!dragging.isEmpty()) {
            int highlightSlot = -1;
            if(dragging.getItem() instanceof AttachmentItem) {
                AttachmentItem item = (AttachmentItem) dragging.getItem();
                GunAttachments attachments = ((GunItem) displayStack.getItem()).getAttachmentList();
                if(attachments.canAttach(item)) {
                    highlightSlot = item.getCategory().ordinal();
                }
            }
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();
            GlStateManager.disableTexture();
            GlStateManager.lineWidth(minecraft.gameSettings.guiScale);
            for (int i = 0; i < 5; i++) {
                Slot slot = container.inventorySlots.get(i);
                boolean b = highlightSlot == i;
                builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                builder.pos(guiLeft + slot.xPos - 1, guiTop + slot.yPos + 17, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos, guiTop + slot.yPos + 17, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos, guiTop + slot.yPos - 1, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos - 1, guiTop + slot.yPos - 1, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos + 16, guiTop + slot.yPos + 17, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos + 17, guiTop + slot.yPos + 17, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos + 17, guiTop + slot.yPos - 1, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos + 16, guiTop + slot.yPos - 1, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos - 1, guiTop + slot.yPos, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos + 17, guiTop + slot.yPos, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos + 17, guiTop + slot.yPos - 1, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos - 1, guiTop + slot.yPos - 1, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos - 1, guiTop + slot.yPos + 17, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos + 17, guiTop + slot.yPos + 17, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos + 17, guiTop + slot.yPos + 16, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                builder.pos(guiLeft + slot.xPos - 1, guiTop + slot.yPos + 16, 0).color(b ? 0.0F : 1.0F, b ? 1.0F : 0.0F, 0.0F, 1.0F).endVertex();
                tessellator.draw();
            }
            GlStateManager.enableTexture();
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
}
