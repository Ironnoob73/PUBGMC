package dev.toma.pubgmc.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.FactoryContainer;
import dev.toma.pubgmc.common.tileentity.AbstractFactoryTileEntity;
import dev.toma.pubgmc.util.RenderHelper;
import dev.toma.pubgmc.util.recipe.PMCRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class FactoryScreen extends ContainerScreen<FactoryContainer> {

    private static final ResourceLocation TEXTURE = Pubgmc.makeResource("textures/screen/factory.png");
    private final AbstractFactoryTileEntity tileEntity;
    private final List<PMCRecipe> allRecipes;
    private final boolean canFitOnScreen;
    private ItemStack selectedOutput = ItemStack.EMPTY;
    private int scrollIndex;

    public FactoryScreen(FactoryContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
        xSize = 252;
        ySize = 191;
        this.tileEntity = container.getTileEntity();
        this.allRecipes = tileEntity.getRecipeList();
        this.canFitOnScreen = allRecipes.size() < 7;
    }

    @Override
    protected void init() {
        super.init();
        for (int i = scrollIndex; i < scrollIndex + 6; i++) {
            if(i >= allRecipes.size())
                break;
            int j = i - scrollIndex;
            PMCRecipe recipe = allRecipes.get(i);
            addButton(new RecipeButton(guiLeft + 180, guiTop + 36 + j * 20, canFitOnScreen ? 64 : 60, 20, recipe, this));
        }
        addButton(new Button(guiLeft + 7, guiTop + 85, 40, 20, "Craft", p -> {}));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if(!selectedOutput.isEmpty()) {
            minecraft.getItemRenderer().renderItemIntoGUI(selectedOutput, guiLeft + 80, guiTop + 74);
        }
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    static class RecipeButton extends Button {

        final PMCRecipe recipe;

        RecipeButton(int x, int y, int width, int height, PMCRecipe recipe, FactoryScreen screen) {
            super(x, y, width, height, trim(recipe), b -> {
                ItemStack stack = screen.selectedOutput;
                if(stack.isEmpty() || stack.getItem() != recipe.getRaw().getItem()) {
                    screen.selectedOutput = recipe.getRaw();
                } else {
                    screen.selectedOutput = ItemStack.EMPTY;
                }
            });
            this.recipe = recipe;
        }

        @Override
        public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontrenderer = minecraft.fontRenderer;
            minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);
            this.isHovered = p_render_1_ >= this.x && p_render_2_ >= this.y && p_render_1_ < this.x + this.width && p_render_2_ < this.y + this.height;
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.alpha);
            int i = this.getYImage(this.isHovered);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.blit(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            this.blit(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.renderBg(minecraft, p_render_1_, p_render_2_);
            minecraft.getItemRenderer().renderItemIntoGUI(recipe.getRaw(), this.x + 2, this.y + 2);
            int j = getFGColor();
            fontrenderer.drawString(this.getMessage(), this.x + 18, this.y + (this.height - 8) / 2f, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
            if(isHovered) {
                String text = recipe.getRaw().getDisplayName().getFormattedText();
                int px = this.x + this.width / 2;
                RenderHelper.drawColoredShape(px, this.y - 12, px + fontrenderer.getStringWidth(text) + 6, this.y, 0.0F, 0.0F, 0.0F, 1.0F);
                fontrenderer.drawString(text, px + 3, this.y - 10, 0xffffff);
            }
        }

        static String trim(PMCRecipe recipe) {
            String in = recipe.getRaw().getDisplayName().getFormattedText();
            if(in.length() < 10) {
                return in;
            }
            return in.substring(0,8) + "...";
        }
    }
}
