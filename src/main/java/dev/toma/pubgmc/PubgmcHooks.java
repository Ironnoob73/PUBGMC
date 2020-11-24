package dev.toma.pubgmc;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.InventoryFactory;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.client.render.layer.BackpackLayer;
import dev.toma.pubgmc.client.render.layer.GhillieLayer;
import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class PubgmcHooks {

    public static ItemCameraTransforms.TransformType renderingType;

    public static void preRenderItem(ItemCameraTransforms.TransformType transformType) {
        renderingType = transformType;
    }

    public static void postRenderItem() {
        renderingType = null;
    }

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
        boolean holdingWeapon = entity.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() instanceof AbstractGunItem;
        boolean isPlayer = entity instanceof PlayerEntity;
        RenderHelper.processEntityModelRotations(isPlayer, holdingWeapon, entity, model);
    }

    public static void setupRotations(AbstractClientPlayerEntity player) {
        IPlayerCap cap = PlayerCapFactory.get(player);
        if(cap.isProne()) {
            GlStateManager.rotated(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translated(0, -1.0, 0.1);
        }
    }
}
