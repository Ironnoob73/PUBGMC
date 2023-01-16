package toma.pubgmc.client.render.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ItemUseTimerOverlay implements IGuiOverlay {

    public static final DecimalFormat TIMER_FORMATTER;

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = gui.getMinecraft();
        Player player = minecraft.player;
        int remainingTime = player.getUseItemRemainingTicks();
        int usingTime = player.getTicksUsingItem();
        int total = remainingTime + usingTime;
        if (total == 0) {
            return;
        }
        float seconds = remainingTime / 20.0F;
        Font font = gui.getFont();
        String formattedText = TIMER_FORMATTER.format(seconds);
        int textWidth = font.width(formattedText);
        int textLeft = (screenWidth - textWidth) / 2;
        int textTop = (screenHeight / 2) + 10;
        GuiComponent.fill(poseStack, textLeft - 2, textTop - 2, textLeft + textWidth + 1, textTop + font.lineHeight + 1, 0x66 << 24);
        font.draw(poseStack, formattedText, textLeft, textTop, 0xFFFFFF);
        float progress = usingTime / (float) total;
        GuiComponent.fill(poseStack, textLeft - 2, textTop + font.lineHeight, textLeft + textWidth + 1, textTop + font.lineHeight + 1, 0xFFAAAAAA);
        GuiComponent.fill(poseStack, textLeft - 2, textTop + font.lineHeight, textLeft - 2 + (int) ((3 + textWidth) * progress), textTop + font.lineHeight + 1, 0xFFFFFFFF);
    }

    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator('.');
        TIMER_FORMATTER = new DecimalFormat("0.0", formatSymbols);
    }
}
