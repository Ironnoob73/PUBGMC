package dev.toma.pubgmc.client.render.entity;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.client.model.entity.BotModel;
import dev.toma.pubgmc.client.render.layer.BackpackLayer;
import dev.toma.pubgmc.client.render.layer.GhillieLayer;
import dev.toma.pubgmc.common.entity.BotEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

@SuppressWarnings("unchecked")
public class BotRenderer<B extends BotEntity, M extends BotModel<B>> extends BipedRenderer<B, M> {

    private static final ResourceLocation[] BOT_TEXTURES = {
            new ResourceLocation(Pubgmc.MODID, "textures/entity/bot_0.png"),
            new ResourceLocation(Pubgmc.MODID, "textures/entity/bot_1.png"),
            new ResourceLocation(Pubgmc.MODID, "textures/entity/bot_2.png"),
            new ResourceLocation(Pubgmc.MODID, "textures/entity/bot_3.png")
    };

    public BotRenderer(EntityRendererManager renderer, M m) {
        super(renderer, m, 0.8F);
        this.addLayer(new BackpackLayer<>(this, BotEntity::getInventory));
        this.addLayer(new GhillieLayer<>(this, BotEntity::getInventory));
    }

    public BotRenderer(EntityRendererManager renderer) {
        this(renderer, (M) new BotModel<B>());
    }

    @Override
    protected ResourceLocation getEntityTexture(B entity) {
        return BOT_TEXTURES[entity.getVariant()];
    }
}
