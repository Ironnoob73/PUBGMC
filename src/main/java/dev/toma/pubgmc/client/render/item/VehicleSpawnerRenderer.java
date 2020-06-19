package dev.toma.pubgmc.client.render.item;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.common.entity.vehicle.DriveableEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class VehicleSpawnerRenderer extends ItemStackTileEntityRenderer {

    public Map<Item, Class<? extends Entity>> ITEM_TO_CLASS_MAP = new HashMap<>();
    private List<EntryHolder<Supplier<Item>, Class<? extends Entity>>> entryHolderList = new ArrayList<>();

    public VehicleSpawnerRenderer add(Supplier<Item> k, Class<? extends DriveableEntity> v) {
        entryHolderList.add(new EntryHolder<>(k, v));
        return this;
    }

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        this.updateItemClassMap();
        Class<? extends Entity> entityClass = ITEM_TO_CLASS_MAP.get(itemStackIn.getItem());
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

    private void updateItemClassMap() {
        if(entryHolderList != null) {
            entryHolderList.forEach(e -> ITEM_TO_CLASS_MAP.put(e.key.get(), e.value));
            entryHolderList = null;
        }
    }

    private static class EntryHolder<K, V> {
        K key; V value;
        private EntryHolder(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
