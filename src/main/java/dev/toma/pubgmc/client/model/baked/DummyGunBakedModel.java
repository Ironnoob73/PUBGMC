package dev.toma.pubgmc.client.model.baked;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;

public class DummyGunBakedModel extends DummyBakedModel {

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        TRSRTransformation transformation = new TRSRTransformation(matrix4f);
        switch (cameraTransformType) {
            case THIRD_PERSON_RIGHT_HAND: case THIRD_PERSON_LEFT_HAND: {
                GlStateManager.translatef(0.0F, -0.175F, 0.1F);
                GlStateManager.scalef(0.75F, 0.75F, 0.75F);
                break;
            }
            case GUI: {
                GlStateManager.translatef(-0.05F, -0.15F, 0.0F);
                GlStateManager.rotatef(90.0F, 0.5F, 1.0F, 0.0F);
                GlStateManager.scalef(0.6F, 0.6F, 0.6F);
                break;
            }
        }
        return Pair.of(this, transformation.getMatrixVec());
    }
}
