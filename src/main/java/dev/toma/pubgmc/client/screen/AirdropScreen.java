package dev.toma.pubgmc.client.screen;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.container.AirdropContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class AirdropScreen extends BasicContainerScreen<AirdropContainer> {

    private static final ResourceLocation RL = Pubgmc.makeResource("textures/screen/airdrop.png");

    public AirdropScreen(AirdropContainer container, PlayerInventory inventory, ITextComponent textComponent) {
        super(container, inventory, textComponent);
        ySize = 133;
    }

    @Override
    public ResourceLocation getTexture() {
        return RL;
    }
}
