package dev.toma.pubgmc.client.render;

import dev.toma.pubgmc.api.entity.IVariantVehicle;
import dev.toma.pubgmc.common.entity.vehicle.DriveableEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public abstract class VariantDriveableRenderer<E extends DriveableEntity & IVariantVehicle> extends DriveableRenderer<E> {

    private ResourceLocation[] textures;

    public VariantDriveableRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(E entity) {
        if(textures == null) {
            this.textures = entity.getTextures();
        }
        return textures[entity.getVariant()];
    }
}
