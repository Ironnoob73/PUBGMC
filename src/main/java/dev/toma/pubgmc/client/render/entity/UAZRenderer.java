package dev.toma.pubgmc.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.client.model.entity.DriveableModel;
import dev.toma.pubgmc.client.model.entity.UAZModel;
import dev.toma.pubgmc.client.render.DriveableRenderer;
import dev.toma.pubgmc.common.entity.vehicle.land.UAZEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class UAZRenderer extends DriveableRenderer<UAZEntity> {

    public static final UAZModel UAZ = new UAZModel();
    private static final ResourceLocation TEXTURE = Pubgmc.makeResource("textures/entity/uaz.png");

    public UAZRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public DriveableModel<UAZEntity> getModel() {
        return UAZ;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(UAZEntity entity) {
        return TEXTURE;
    }

    @Override
    public void prepareRender() {
        GlStateManager.translated(0, 1.4, 0);
    }
}
