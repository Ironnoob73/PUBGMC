package dev.toma.pubgmc;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.InventoryFactory;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.client.render.layer.BackpackLayer;
import dev.toma.pubgmc.client.render.layer.GhillieLayer;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class PubgmcHooks {

    /**
     * Called from {@link LivingRenderer}'s constructor
     * @param renderer - the renderer instance
     */
    public static void onLivingRenderCreated(LivingRenderer<?, ?> renderer) {
        if(renderer instanceof PlayerRenderer) {
            PlayerRenderer playerRenderer = (PlayerRenderer) renderer;
            playerRenderer.addLayer(new GhillieLayer<>(playerRenderer, InventoryFactory::getInventoryHandler));
            playerRenderer.addLayer(new BackpackLayer<>(playerRenderer, InventoryFactory::getInventoryHandler));
        }
    }

    /**
     * Called from {@link BipedModel#setRotationAngles(LivingEntity, float, float, float, float, float, float)} just before return statement
     * @param model Model being rendered
     * @param entity Entity instance
     */
    public static void onSetupRotationAngles(BipedModel<?> model, LivingEntity entity) {
        boolean holdingWeapon = entity.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() instanceof GunItem;
        boolean isPlayer = entity instanceof PlayerEntity;
        if(isPlayer) {
            IPlayerCap cap = PlayerCapFactory.get((PlayerEntity) entity);
            boolean isProne = cap.isProne();
            if(isProne) {
                float f0 = (float) Math.toRadians(180.0F);
                float f1 = (float) Math.toRadians(10.0F);
                float f2 = (float) Math.toRadians(-45.0F);
                model.bipedRightArm.rotateAngleX = f0;
                model.bipedLeftArm.rotateAngleX = f0;
                model.bipedRightArm.rotateAngleZ = -f1;
                model.bipedLeftArm.rotateAngleZ = f1;
                model.bipedRightLeg.rotateAngleZ = f1;
                model.bipedLeftLeg.rotateAngleZ = -f1;
                model.bipedHead.rotateAngleX = model.bipedHead.rotateAngleX + f2;
                model.bipedHeadwear.rotateAngleX = model.bipedHead.rotateAngleX;
                entity.limbSwing = 0.0F;
                entity.limbSwingAmount = 0.0F;
            } else if(holdingWeapon) {
                boolean aiming = cap.getAimInfo().isActualAim();
                float f0;
                float f1;
                float f2;
                if(aiming) {
                    f0 = (float) Math.toRadians(-90.0F);
                    f1 = (float) Math.toRadians(-30.0F);
                    f2 = (float) Math.toRadians(45.0F);
                    model.bipedRightArm.rotateAngleX = f0;
                    model.bipedRightArm.rotateAngleY = f1;
                    model.bipedLeftArm.rotateAngleX = f0;
                } else {
                    f0 = (float) Math.toRadians(-55.0F);
                    f1 = (float) Math.toRadians(-40.0F);
                    f2 = (float) Math.toRadians(60.0F);
                    float f3 = (float) Math.toRadians(-60.0F);
                    model.bipedRightArm.rotateAngleX = f0;
                    model.bipedLeftArm.rotateAngleX = f3;
                    model.bipedRightArm.rotateAngleY = f1;
                }
                model.bipedLeftArm.rotateAngleY = f2;
            }
        } else {
            if(holdingWeapon) {
                float f0 = (float) Math.toRadians(-90.0F);
                float f1 = (float) Math.toRadians(-30.0F);
                float f2 = (float) Math.toRadians(45.0F);
                model.bipedRightArm.rotateAngleX = f0;
                model.bipedRightArm.rotateAngleY = f1;
                model.bipedLeftArm.rotateAngleX = f0;
                model.bipedLeftArm.rotateAngleY = f2;
            }
        }
    }

    /**
     * Called from player's renderer
     * @param player
     */
    public static void setupRotations(AbstractClientPlayerEntity player) {
        IPlayerCap cap = PlayerCapFactory.get(player);
        if(cap.isProne()) {
            GlStateManager.rotated(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translated(0, -1.0, 0.1);
        }
    }
}
