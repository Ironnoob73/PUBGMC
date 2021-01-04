package dev.toma.pubgmc.client.model.gun.attachment;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.render.item.GunRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.util.ResourceLocation;

public abstract class AttachmentModel extends Model {

    protected abstract void render();

    protected void preRenderCallback() {

    }

    public final void doRender() {
        GlStateManager.pushMatrix();
        GlStateManager.translated(0, 1, 0);
        GlStateManager.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scalef(0.01f, 0.01f, 0.01f);
        this.preRenderCallback();
        Minecraft.getInstance().getTextureManager().bindTexture(this.getAttachmentTexture());
        this.render();
        GlStateManager.popMatrix();
    }

    public ResourceLocation getAttachmentTexture() {
        return GunRenderer.ATTACHMENT_TEXTURE_MAP;
    }

    public static void setRotationAngle(RendererModel rendererModel, float x, float y, float z) {
        rendererModel.rotateAngleX = x;
        rendererModel.rotateAngleY = y;
        rendererModel.rotateAngleZ = z;
    }
}
