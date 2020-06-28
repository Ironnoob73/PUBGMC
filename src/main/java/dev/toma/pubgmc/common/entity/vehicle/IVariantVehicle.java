package dev.toma.pubgmc.common.entity.vehicle;

import net.minecraft.util.ResourceLocation;

public interface IVariantVehicle {

    ResourceLocation[] getTextures();

    void setVariant();

    int getVariant();
}
