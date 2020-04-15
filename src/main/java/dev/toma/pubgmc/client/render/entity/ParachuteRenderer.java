package dev.toma.pubgmc.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.client.model.entity.ParachuteModel;
import dev.toma.pubgmc.common.entity.ParachuteEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ParachuteRenderer extends EntityRenderer<ParachuteEntity> {

    private static final ResourceLocation TEXTURE = Pubgmc.makeResource("textures/entity/parachute.png");
    private final ParachuteModel model;

    public ParachuteRenderer(EntityRendererManager manager) {
        super(manager);
        this.model = new ParachuteModel();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(ParachuteEntity entity) {
        return TEXTURE;
    }

    @Override
    public void doRender(ParachuteEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        bindEntityTexture(entity);
        int deployTime = Math.min(entity.ticksExisted, 6);
        double mod = deployTime / 6.0D;
        double prevMod = mod - 1 / 6D;
        double smooth = mod == 1 ? 1 : prevMod + (mod - prevMod) * partialTicks;
        GlStateManager.translated(x, y, z);
        GlStateManager.translated(0, 3.7, 0);
        GlStateManager.rotated(180, 1.0, 0.0, 0.0);
        GlStateManager.scaled(0.07 * smooth, 0.07 * smooth, 0.07 * smooth);
        GlStateManager.rotatef(entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
        if(entity.timeWithoutOwner > 0) {
            double land = entity.timeWithoutOwner / 100.0D;
            double prevLand = land - 0.01D;
            double interpolatedLand = prevLand + (land - prevLand) * partialTicks;
            GlStateManager.translated(0, 45 * interpolatedLand, 0);
            GlStateManager.rotated(90 * interpolatedLand, 1, 0, 0);
        }
        GlStateManager.disableLighting();
        model.renderChute();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
