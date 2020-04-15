package dev.toma.pubgmc.client.render.entity;

import dev.toma.pubgmc.common.entity.ParachuteEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ParachuteRenderer extends EntityRenderer<ParachuteEntity> {

    public ParachuteRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(ParachuteEntity entity) {
        return null;
    }

    @Override
    public void doRender(ParachuteEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {

    }
}
