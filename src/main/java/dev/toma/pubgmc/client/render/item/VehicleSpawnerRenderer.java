package dev.toma.pubgmc.client.render.item;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.common.entity.vehicle.AirDriveableEntity;
import dev.toma.pubgmc.common.entity.vehicle.LandDriveableEntity;
import dev.toma.pubgmc.init.PMCItems;
import dev.toma.pubgmc.util.object.LazyLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class VehicleSpawnerRenderer extends ItemStackTileEntityRenderer {

    public static VehicleSpawnerRenderer renderer = new VehicleSpawnerRenderer();
    public LazyLoader<Map<Item, Class<? extends Entity>>> ITEM_TO_CLASS_MAP_LOADER = new LazyLoader<>(() -> {
        Map<Item, Class<? extends Entity>> map = new HashMap<>();
        map.put(PMCItems.SPAWN_UAZ, LandDriveableEntity.UAZDriveable.class);
        map.put(PMCItems.SPAWN_GLIDER, AirDriveableEntity.GliderDriveable.class);
        return map;
    });

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        Class<? extends Entity> entityClass = ITEM_TO_CLASS_MAP_LOADER.get().get(itemStackIn.getItem());
        if(entityClass == null) return;
        Minecraft mc = Minecraft.getInstance();
        EntityRendererManager rendererManager = mc.getRenderManager();
        EntityRenderer<?> renderer = rendererManager.getRenderer(entityClass);
        GlStateManager.pushMatrix();
        GlStateManager.translated(0.5, 0.25, 0.0);
        GlStateManager.rotated(-45.0, 0.0, 1.0, 0.0);
        GlStateManager.rotated(5.0, 1.0, 0.0, 0.0);
        GlStateManager.scaled(0.15, 0.15, 0.15);
        renderer.doRender(null, 0, 0, 0, 0.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
