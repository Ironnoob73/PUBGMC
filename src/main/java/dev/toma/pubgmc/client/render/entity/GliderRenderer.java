package dev.toma.pubgmc.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.client.model.entity.DriveableModel;
import dev.toma.pubgmc.client.model.entity.GliderModel;
import dev.toma.pubgmc.client.render.DriveableRenderer;
import dev.toma.pubgmc.common.entity.vehicle.air.GliderEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class GliderRenderer extends DriveableRenderer<GliderEntity> {

    private static final ResourceLocation TEXTURE = Pubgmc.makeResource("textures/entity/glider.png");
    public static final GliderModel GLIDER = new GliderModel();

    public GliderRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public DriveableModel<GliderEntity> getModel() {
        return GLIDER;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(GliderEntity entity) {
        return TEXTURE;
    }

    @Override
    public void prepareRender() {
        GlStateManager.scaled(2, 2, 2);
        GlStateManager.translated(0, 1.5, 0);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }
}
