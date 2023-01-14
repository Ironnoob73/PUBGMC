package toma.pubgmc.client.render.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import toma.pubgmc.api.capability.PubgmcCapabilities;
import toma.pubgmc.client.PubgmcClient;
import toma.pubgmc.common.capability.PlayerBoostStats;
import toma.pubgmc.config.PubgmcClientConfig;
import toma.pubgmc.util.ColorUtil;

public class BoostOverlay implements IGuiOverlay {

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        Minecraft client = gui.getMinecraft();
        if (client.options.hideGui || !gui.shouldDrawSurvivalElements()) {
            return;
        }
        Player player = client.player;
        player.getCapability(PubgmcCapabilities.BOOST_STATS).ifPresent(stats -> {
            float boostValue = stats.getBoostValue();
            if (boostValue <= 0.0F)
                return;
            float value = boostValue / PlayerBoostStats.MAX_BOOST_VALUE;
            int xHalf = 91;
            int left = screenWidth / 2 - xHalf;
            int top = screenHeight - gui.leftHeight + 15;
            PubgmcClientConfig config = PubgmcClient.getConfig();
            int bgColor = ColorUtil.decodeColor24(config.boostOverlayColorBG);
            int fgColor = ColorUtil.decodeColor24(config.boostOverlayColorFG);
            GuiComponent.fill(poseStack, left, top, left + 2 * xHalf, top + 2, bgColor);
            GuiComponent.fill(poseStack, left, top, left + (int) ((2 * xHalf) * value), top + 2, fgColor);
        });
    }
}
