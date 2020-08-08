package dev.toma.pubgmc.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.client.model.entity.AirdropModel;
import dev.toma.pubgmc.common.entity.AirdropEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class AirdropRenderer extends EntityRenderer<AirdropEntity> {

    private final ResourceLocation normalTextures = Pubgmc.makeResource("textures/entity/airdrop_normal.png");
    private final ResourceLocation flareTextures = Pubgmc.makeResource("textures/entity/airdrop_flare.png");
    private final AirdropModel.Normal normal;
    private final AirdropModel.Flare flare;

    public AirdropRenderer(EntityRendererManager manager) {
        super(manager);
        normal = new AirdropModel.Normal();
        flare = new AirdropModel.Flare();
    }

    public boolean isFlareType(AirdropEntity entity) {
        return entity.getAirdropType() == AirdropEntity.AirdropType.FLARE;
    }

    public AirdropModel getModel(AirdropEntity entity) {
        return isFlareType(entity) ? flare : normal;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(AirdropEntity entity) {
        return isFlareType(entity) ? flareTextures : normalTextures;
    }

    @Override
    public void doRender(AirdropEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        GlStateManager.translatef(0, 1.9f, 0);
        GlStateManager.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scalef(0.08F, 0.08F, 0.08F);
        bindEntityTexture(entity);
        getModel(entity).render();
        GlStateManager.popMatrix();
    }
}
