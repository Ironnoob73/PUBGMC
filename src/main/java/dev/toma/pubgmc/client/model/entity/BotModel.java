package dev.toma.pubgmc.client.model.entity;

import dev.toma.pubgmc.common.entity.BotEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;

public class BotModel<B extends BotEntity> extends BipedModel<B> {

    public BotModel() {
        super(0.0F);
    }

    @Override
    public void setRotationAngles(B entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
    }
}
