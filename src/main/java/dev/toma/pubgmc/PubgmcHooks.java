package dev.toma.pubgmc;

import dev.toma.pubgmc.capability.player.InventoryFactory;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.client.render.layer.BackpackLayer;
import dev.toma.pubgmc.client.render.layer.GhillieLayer;
import dev.toma.pubgmc.common.item.gun.GunItem;
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
        if(holdingWeapon) {
            if(entity instanceof PlayerEntity) {
                boolean aiming = PlayerCapFactory.get((PlayerEntity) entity).getAimInfo().isAiming();
            } else {
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
}
