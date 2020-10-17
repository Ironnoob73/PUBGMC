package dev.toma.pubgmc.client.render.item;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.common.entity.vehicle.DriveableEntity;
import dev.toma.pubgmc.common.item.utility.VehicleSpawnerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VehicleSpawnerRenderer extends ItemStackTileEntityRenderer {

    public static VehicleSpawnerRenderer renderer = new VehicleSpawnerRenderer();

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        Class<? extends DriveableEntity> entityClass = ((VehicleSpawnerItem) itemStackIn.getItem()).getVehicleClass();
        if(entityClass == null) return;
        Minecraft mc = Minecraft.getInstance();
        EntityRendererManager rendererManager = mc.getRenderManager();
        EntityRenderer<?> renderer = rendererManager.getRenderer(entityClass);
        GlStateManager.pushMatrix();
        GlStateManager.translated(0.5, 0.25, 0.0);
        GlStateManager.rotated(30.0, 1.0, 0.0, 0.0);
        GlStateManager.rotated(-45.0, 0.0, 1.0, 0.0);
        GlStateManager.scaled(0.15, 0.15, 0.15);
        renderer.doRender(null, 0, 0, 0, 0.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
