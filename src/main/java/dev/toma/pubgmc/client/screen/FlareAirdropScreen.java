package dev.toma.pubgmc.client.screen;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.FlareAirdropContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FlareAirdropScreen extends BasicContainerScreen<FlareAirdropContainer> {

    private static final ResourceLocation RL = Pubgmc.makeResource("textures/screen/flare_airdrop.png");

    public FlareAirdropScreen(FlareAirdropContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
    }

    @Override
    public ResourceLocation getTexture() {
        return RL;
    }
}
