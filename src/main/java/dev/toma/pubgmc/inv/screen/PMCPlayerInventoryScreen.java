package dev.toma.pubgmc.inv.screen;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.inv.container.PMCPlayerContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PMCPlayerInventoryScreen extends DisplayEffectsScreen<PMCPlayerContainer> implements IRecipeShownListener {

    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
    protected float oldMouseX;
    protected float oldMouseY;
    private final RecipeBookGui recipeBookGui = new RecipeBookGui();
    private boolean removeRecipeBookGui;
    private boolean widthTooNarrow;
    private boolean buttonClicked;

    public PMCPlayerInventoryScreen(PMCPlayerContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    public void tick() {
        if (this.minecraft.playerController.isInCreativeMode()) {
            this.minecraft.displayGuiScreen(new CreativeScreen(this.minecraft.player));
        } else {
            this.recipeBookGui.tick();
        }
    }

    @Override
    protected void init() {
        if (this.minecraft.playerController.isInCreativeMode()) {
            this.minecraft.displayGuiScreen(new CreativeScreen(this.minecraft.player));
        } else {
            super.init();
            this.widthTooNarrow = this.width < 379;
            this.recipeBookGui.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.container);
            this.removeRecipeBookGui = true;
            this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
            this.children.add(this.recipeBookGui);
            this.setFocusedDefault(this.recipeBookGui);
            this.addButton(new ImageButton(this.guiLeft + 104, this.height / 2 - 22, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (p_214086_1_) -> {
                this.recipeBookGui.initSearchBar(this.widthTooNarrow);
                this.recipeBookGui.toggleVisibility();
                this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
                ((ImageButton)p_214086_1_).setPosition(this.guiLeft + 104, this.height / 2 - 22);
                this.buttonClicked = true;
            }));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 97.0F, 8.0F, 4210752);
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        this.hasActivePotionEffects = !this.recipeBookGui.isVisible();
        if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
            this.drawGuiContainerBackgroundLayer(p_render_3_, p_render_1_, p_render_2_);
            this.recipeBookGui.render(p_render_1_, p_render_2_, p_render_3_);
        } else {
            this.recipeBookGui.render(p_render_1_, p_render_2_, p_render_3_);
            super.render(p_render_1_, p_render_2_, p_render_3_);
            this.recipeBookGui.renderGhostRecipe(this.guiLeft, this.guiTop, false, p_render_3_);
        }

        this.renderHoveredToolTip(p_render_1_, p_render_2_);
        this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, p_render_1_, p_render_2_);
        this.oldMouseX = (float)p_render_1_;
        this.oldMouseY = (float)p_render_2_;
        this.func_212932_b(this.recipeBookGui);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        drawEntityOnScreen(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.minecraft.player);
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)posX, (float)posY, 50.0F);
        GlStateManager.scalef((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(-((float)Math.atan((mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float)Math.atan((mouseX / 40.0F)) * 20.0F;
        ent.rotationYaw = (float)Math.atan((mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float)Math.atan((mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translatef(0.0F, 0.0F, 0.0F);
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        entityrenderermanager.setPlayerViewY(180.0F);
        entityrenderermanager.setRenderShadow(false);
        entityrenderermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        entityrenderermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.disableTexture();
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
    }

    @Override
    protected boolean isPointInRegion(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.isPointInRegion(x, y, width, height, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if (this.recipeBookGui.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)) {
            return true;
        } else {
            return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
        }
    }

    @Override
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        } else {
            return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.xSize) || mouseY >= (double)(guiTopIn + this.ySize);
        return this.recipeBookGui.func_195604_a(mouseX, mouseY, this.guiLeft, this.guiTop, this.xSize, this.ySize, mouseButton) && flag;
    }

    @Override
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
        this.recipeBookGui.slotClicked(slotIn);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookGui.recipesUpdated();
    }

    @Override
    public void removed() {
        if (this.removeRecipeBookGui) {
            this.recipeBookGui.removed();
        }

        super.removed();
    }

    @Override
    public RecipeBookGui getRecipeGui() {
        return this.recipeBookGui;
    }
}
