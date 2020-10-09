package dev.toma.pubgmc.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.FactoryContainer;
import dev.toma.pubgmc.common.tileentity.AbstractFactoryTileEntity;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.SPacketCraft;
import dev.toma.pubgmc.util.RenderHelper;
import dev.toma.pubgmc.data.recipe.PMCRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;

public class FactoryScreen extends ContainerScreen<FactoryContainer> {

    public static final ResourceLocation TEXTURE = Pubgmc.makeResource("textures/screen/factory.png");
    private final AbstractFactoryTileEntity tileEntity;
    private final List<PMCRecipe> allRecipes;
    private final boolean canFitOnScreen;
    private PMCRecipe selectedRecipe;
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
        addButton(new Button(guiLeft + 7, guiTop + 85, 40, 20, "Craft", p -> {
            if(selectedRecipe != null) {
                List<ItemStack> ingredients = selectedRecipe.ingredientList();
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(inventory -> {
                    boolean canCraft = true;
                    for (ItemStack stack : ingredients) {
                        Item item = stack.getItem();
                        int count = 0;
                        for (int i = 0; i < 8; i++) {
                            ItemStack stack1 = inventory.getStackInSlot(i);
                            if(!stack1.isEmpty()) {
                                if(stack1.getItem() == item) {
                                    count += stack1.getCount();
                                    if(count >= stack.getCount()) {
                                        break;
                                    }
                                }
                            }
                        }
                        if(count < stack.getCount()) {
                            canCraft = false;
                            break;
                        }
                    }
                    if(canCraft) {
                        NetworkManager.sendToServer(new SPacketCraft(tileEntity.getPos(), selectedRecipe));
                    }
                });
            }
        }));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(TEXTURE);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if(!canFitOnScreen) {
            RenderHelper.drawColoredShape(242, 36, 246, 156, 0.0F, 0.0F, 0.0F, 1.0F);
            double step = 120.0D / allRecipes.size();
            double start = scrollIndex * step;
            double end = (scrollIndex + 6) * step;
            RenderHelper.drawColoredShape(243, 37 + start, 245, 35 + end, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        if(selectedRecipe != null) {
            boolean canFit = guiLeft - 40 > 100;
            int top = guiTop + 5;
            RenderHelper.drawColoredShape(20, guiTop, guiLeft - 20, guiTop + ySize, 0.0F, 0.0F, 0.0F, 0.65F);
            ItemStack raw = selectedRecipe.getRaw();
            if(canFit) {
                font.drawString(raw.getDisplayName().getFormattedText() + " (" + raw.getCount() + "x)", 25, top, 0xffffff);
            } else {
                net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                minecraft.getItemRenderer().renderItemIntoGUI(raw, 20, top - 3);
                net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            }
            int j = 0;
            for (ItemStack stack : selectedRecipe.ingredientList()) {
                int textY = top + 10 + j * 20;
                String text = canFit ? String.format("%dx %s", stack.getCount(), stack.getDisplayName().getFormattedText()) : stack.getCount() + "x";
                net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                minecraft.getItemRenderer().renderItemIntoGUI(stack, 24, textY);
                net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
                font.drawString(text, 43, textY + 4, 0xffffff);
                ++j;
            }
        }
        for (Widget widget : buttons) {
            if(widget.isHovered() && widget instanceof RecipeButton) {
                RecipeButton button = (RecipeButton) widget;
                String text = button.recipe.getRaw().getDisplayName().getFormattedText();
                int px = button.x + button.getWidth() / 2;
                RenderHelper.drawColoredShape(px, button.y - 12, px + font.getStringWidth(text) + 6, button.y, 0.0F, 0.0F, 0.0F, 1.0F);
                font.drawString(text, px + 3, button.y - 10, 0xffffff);
            }
        }
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int i = -(int)(amount);
        int j = scrollIndex + i;
        if(j >= 0 && j <= allRecipes.size() - 6) {
            this.scrollIndex = j;
            this.init(minecraft, width, height);
            return true;
        }
        return false;
    }

    static class RecipeButton extends Button {

        final PMCRecipe recipe;

        RecipeButton(int x, int y, int width, int height, PMCRecipe recipe, FactoryScreen screen) {
            super(x, y, width, height, trim(recipe), b -> {
                PMCRecipe pmcRecipe = screen.selectedRecipe;
                if(pmcRecipe == null || pmcRecipe.getRaw().getItem() != recipe.getRaw().getItem()) {
                    screen.selectedRecipe = recipe;
                } else {
                    screen.selectedRecipe = null;
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
