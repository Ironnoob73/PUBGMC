package dev.toma.pubgmc.client.animation;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface HandAnimate {

    @OnlyIn(Dist.CLIENT)
    void renderRightArm();

    @OnlyIn(Dist.CLIENT)
    void renderLeftArm();

    @OnlyIn(Dist.CLIENT)
    default void renderHand(HandSide side) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(mc.player.getLocationSkin());
        EntityRenderer<AbstractClientPlayerEntity> render = mc.getRenderManager().getRenderer(mc.player);
        PlayerRenderer playerRenderer = (PlayerRenderer) render;
        GlStateManager.pushMatrix();
        GlStateManager.rotatef(40.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        if (side == HandSide.RIGHT) {
            GlStateManager.translatef(0.8F, -0.3F, -0.4F);
            GlStateManager.rotatef(-41.0F, 0.0F, 0.0F, 1.0F);
            playerRenderer.renderRightArm(mc.player);
        } else {
            GlStateManager.translatef(-0.5F, 0.6F, -0.36F);
            GlStateManager.rotatef(-41.0F, 0.0F, 0.0F, 1.0F);
            playerRenderer.renderLeftArm(mc.player);
        }
        GlStateManager.popMatrix();
    }
}
