package dev.toma.pubgmc.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.model.entity.DriveableModel;
import dev.toma.pubgmc.common.entity.vehicle.DriveableEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.MathHelper;

public abstract class DriveableRenderer<T extends DriveableEntity> extends EntityRenderer<T> {

    public DriveableRenderer(EntityRendererManager manager) {
        super(manager);
    }

    public abstract DriveableModel<T> getModel();

    public void prepareRender() {

    }

    @Override
    protected void renderName(T entity, double x, double y, double z) {
        // don't :)
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        {
            GlStateManager.disableLighting();
            bindEntityTexture(entity);
            GlStateManager.translated(x, y, z);
            this.prepareRender();
            GlStateManager.rotated(180, 1, 0, 0);
            GlStateManager.scalef(0.05F, 0.05F, 0.05F);
            GlStateManager.rotatef(entityYaw, 0f, 1f, 0f);
            if(entity != null) GlStateManager.rotatef(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch), 1.0F, 0.0F, 0.0F);
            this.getModel().doRender(entity, partialTicks);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
