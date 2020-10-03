package dev.toma.pubgmc.client.render.entity;

import dev.toma.pubgmc.common.entity.EquipmentHolder;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;

import java.util.function.Function;

public abstract class EquipmentLayer<T extends LivingEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> {

    private final Function<T, EquipmentHolder> inventory;

    public EquipmentLayer(IEntityRenderer<T, M> renderer, Function<T, EquipmentHolder> inventory) {
        super(renderer);
        this.inventory = inventory;
    }

    public EquipmentHolder getInventory(T t) {
        return inventory.apply(t);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
