package dev.toma.pubgmc;

import dev.toma.pubgmc.capability.player.InventoryFactory;
import dev.toma.pubgmc.client.render.layer.BackpackLayer;
import dev.toma.pubgmc.client.render.layer.GhillieLayer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;

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
}
