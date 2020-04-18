package dev.toma.pubgmc.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.client.model.entity.DriveableModel;
import dev.toma.pubgmc.client.model.entity.UAZModel;
import dev.toma.pubgmc.client.render.DriveableRenderer;
import dev.toma.pubgmc.common.entity.vehicle.LandDriveableEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class UAZRenderer extends DriveableRenderer<LandDriveableEntity.UAZDriveable> {

    private final UAZModel model;
    private static final ResourceLocation TEXTURE = Pubgmc.makeResource("textures/entity/uaz.png");

    public UAZRenderer(EntityRendererManager manager) {
        super(manager);
        this.model = new UAZModel();
    }

    @Override
    public DriveableModel<LandDriveableEntity.UAZDriveable> getModel() {
        return model;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LandDriveableEntity.UAZDriveable entity) {
        return TEXTURE;
    }

    @Override
    public void prepareRender() {
        GlStateManager.translated(0, 1.4, 0);
    }
}
