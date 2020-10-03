package dev.toma.pubgmc.client.render.entity;

import dev.toma.pubgmc.client.model.entity.BotModel;
import dev.toma.pubgmc.client.render.layer.BackpackLayer;
import dev.toma.pubgmc.client.render.layer.GhillieLayer;
import dev.toma.pubgmc.common.entity.BotEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;

@SuppressWarnings("unchecked")
public class BotRenderer<B extends BotEntity, M extends BotModel<B>> extends BipedRenderer<B, M> {

    public BotRenderer(EntityRendererManager renderer, M m) {
        super(renderer, m, 0.8F);
        this.addLayer(new BackpackLayer<>(this, BotEntity::getInventory));
        this.addLayer(new GhillieLayer<>(this, BotEntity::getInventory));
    }

    public BotRenderer(EntityRendererManager renderer) {
        this(renderer, (M) new BotModel<B>());
    }
}
