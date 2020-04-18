package dev.toma.pubgmc.api.entity;

import net.minecraft.util.ResourceLocation;

public interface IVariantVehicle {

    ResourceLocation[] getTextures();

    void setVariant();

    int getVariant();
}
