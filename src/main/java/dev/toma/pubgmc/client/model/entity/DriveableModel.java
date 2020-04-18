package dev.toma.pubgmc.client.model.entity;

import dev.toma.pubgmc.common.entity.vehicle.DriveableEntity;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public abstract class DriveableModel<E extends DriveableEntity> extends Model {

    public abstract void doRender(E entity, float partialTicks);

    public void renderFrontWheel(RendererModel wheelRenderer, float turnModifier) {
        setRotationAngle(wheelRenderer, 0f, turnModifier / 5f, 0f);
        wheelRenderer.render(1f);
    }

    public void renderSteeringWheel(RendererModel steeringWheel, float turnModifier) {
        setRotationAngle(steeringWheel, 0f, 0f, -turnModifier / 4.5f);
        steeringWheel.render(1f);
    }

    public static void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
