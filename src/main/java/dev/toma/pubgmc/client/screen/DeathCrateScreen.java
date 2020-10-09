package dev.toma.pubgmc.client.screen;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.DeathCrateContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class DeathCrateScreen extends BasicContainerScreen<DeathCrateContainer> {

    static final ResourceLocation TEXTURE = Pubgmc.makeResource("textures/screen/death_crate.png");

    public DeathCrateScreen(DeathCrateContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
        ySize = 208;
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }
}
