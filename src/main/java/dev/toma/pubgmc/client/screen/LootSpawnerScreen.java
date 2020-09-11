package dev.toma.pubgmc.client.screen;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.LootSpawnerContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class LootSpawnerScreen extends BasicContainerScreen<LootSpawnerContainer> {

    private static final ResourceLocation TEXTURE = Pubgmc.makeResource("textures/screen/loot_spawner.png");

    public LootSpawnerScreen(LootSpawnerContainer container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        ySize = 115;
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }
}
