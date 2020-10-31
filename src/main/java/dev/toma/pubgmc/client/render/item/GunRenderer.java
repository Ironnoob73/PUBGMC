package dev.toma.pubgmc.client.render.item;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.client.model.gun.AbstractGunModel;
import dev.toma.pubgmc.client.model.gun.P92Model;
import dev.toma.pubgmc.client.model.gun.attachment.*;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
import dev.toma.pubgmc.util.AttachmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class GunRenderer extends ItemStackTileEntityRenderer {

    // attachments models
    public static final AttachmentModel SMG_SUPPRESSOR = new SMGSuppressorModel();
    public static final AttachmentModel AR_SUPPRESSOR = new ARSuppressorModel();
    public static final AttachmentModel SR_SUPPRESSOR = new SRSuppressorModel();
    public static final AttachmentModel RED_DOT = new RedDotModel();
    public static final AttachmentModel HOLO = new HolographicModel();
    public static final AttachmentModel SCOPE_2X = new ScopeX2Model();
    public static final AttachmentModel SCOPE_4X = new ScopeX4Model();
    public static final AttachmentModel SCOPE_8X = new ScopeX8Model();
    public static final AttachmentModel SCOPE_15X = new ScopeX15Model();
    public static final AttachmentModel VERTICAL_GRIP = new VerticalGripModel();
    public static final AttachmentModel ANGLED_GRIP = new AngledGripModel();

    public abstract AbstractGunModel getModel();

    public abstract ResourceLocation getTexture();

    protected abstract void renderAttachments(GunItem item, ItemStack stack);

    public void offsetModel() {

    }

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        Minecraft.getInstance().getTextureManager().bindTexture(this.getTexture());
        GlStateManager.pushMatrix();
        this.offsetModel();
        GlStateManager.translatef(0.5F, 1.2F, 0.25F);
        GlStateManager.scalef(0.02F, 0.02F, 0.02F);
        GlStateManager.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
        this.getModel().render(itemStackIn);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.renderAttachments((GunItem) itemStackIn.getItem(), itemStackIn);
        GlStateManager.popMatrix();
    }

    protected static ResourceLocation gunResource(String name) {
        return new ResourceLocation(Pubgmc.MODID, "textures/weapons/" + name + ".png");
    }

    private boolean has(AttachmentCategory category, AttachmentItem item, GunItem gun, ItemStack stack) {
        return gun.getAttachment(category, stack) == item;
    }

    public static class P92Renderer extends GunRenderer {

        private final P92Model model = new P92Model();
        private final ResourceLocation texture = gunResource("p92");

        @Override
        public P92Model getModel() {
            return model;
        }

        @Override
        public ResourceLocation getTexture() {
            return texture;
        }

        @Override
        protected void renderAttachments(GunItem item, ItemStack stack) {
            if(AttachmentHelper.hasRedDot(item, stack)) {
                GlStateManager.pushMatrix();
                GlStateManager.scaled(0.65, 0.65, 0.65);
                GlStateManager.translated(0.768, 0.6, 0.5);
                RED_DOT.doRender();
                GlStateManager.popMatrix();
            }
            if(AttachmentHelper.hasSilencer(item, stack)) {
                SMG_SUPPRESSOR.doRender();
            }
        }
    }
}
