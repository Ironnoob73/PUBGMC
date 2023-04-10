package dev.toma.pubgmc.util.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ImageUtil {
    public static void drawFullScreenImage(Minecraft minecraft, ScaledResolution resolution, ResourceLocation imageLocation, boolean transparent) {
        minecraft.getTextureManager().bindTexture(imageLocation);
        GlStateManager.color(1f, 1f, 1f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(0, resolution.getScaledHeight(), 0).tex(0, 1).endVertex();
        buffer.pos(resolution.getScaledWidth(), resolution.getScaledHeight(), 0).tex(1, 1).endVertex();
        buffer.pos(resolution.getScaledWidth(), 0, 0).tex(1, 0).endVertex();
        buffer.pos(0, 0, 0).tex(0, 0).endVertex();

        if (transparent) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            tessellator.draw();
            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();
        } else tessellator.draw();
    }

    public static void drawFullScreenImage(Minecraft minecraft, ScaledResolution resolution, ResourceLocation imageLocation, float f) {
        minecraft.getTextureManager().bindTexture(imageLocation);
        GlStateManager.color(1f, 1f, 1f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(0, resolution.getScaledHeight(), 0).tex(0, 1).endVertex();
        buffer.pos(resolution.getScaledWidth(), resolution.getScaledHeight(), 0).tex(1, 1).endVertex();
        buffer.pos(resolution.getScaledWidth(), 0, 0).tex(1, 0).endVertex();
        buffer.pos(0, 0, 0).tex(0, 0).endVertex();

        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, f);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.popMatrix();
    }

    public static void drawCustomSizedImage(Minecraft minecraft, ResourceLocation imageLocation, double startX, double startY, double width, double height, boolean transparent) {
        minecraft.getTextureManager().bindTexture(imageLocation);
        GlStateManager.color(1f, 1f, 1f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(startX, startY + height, 0).tex(0, 1).endVertex();
        buffer.pos(startX + width, startY + height, 0).tex(1, 1).endVertex();
        buffer.pos(startX + width, startY, 0).tex(1, 0).endVertex();
        buffer.pos(startX, startY, 0).tex(0, 0).endVertex();

        if (transparent) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            tessellator.draw();
            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();
        } else tessellator.draw();
    }

    public static void drawImageWithUV(Minecraft minecraft, ResourceLocation imageLocation, int startX, int startY, double width, double height, double startU, double startV, double u, double v, boolean transparent) {
        minecraft.getTextureManager().bindTexture(imageLocation);
        GlStateManager.color(1f, 1f, 1f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        if (u > 1) u = 1;
        if (v > 1) v = 1;

        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(startX, startY + height, 0).tex(startU, v).endVertex();
        buffer.pos(startX + width, startY + height, 0).tex(u, v).endVertex();
        buffer.pos(startX + width, startY, 0).tex(u, startV).endVertex();
        buffer.pos(startX, startY, 0).tex(startU, startV).endVertex();

        if (transparent) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            tessellator.draw();
            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();
        } else tessellator.draw();
    }

    public static void drawTintedImage(Minecraft minecraft, ResourceLocation location, int startX, int startY, int width, int height, float r, float g, float b, float a) {
        minecraft.getTextureManager().bindTexture(location);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        builder.pos(startX, startY + height, 0).tex(0.0, 1.0).color(r, g, b, a).endVertex();
        builder.pos(startX + width, startY + height, 0).tex(1.0, 1.0).color(r, g, b, a).endVertex();
        builder.pos(startX + width, startY, 0).tex(1.0, 0.0).color(r, g, b, a).endVertex();
        builder.pos(startX, startY, 0).tex(0.0, 0.0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }

    public static void drawShape(int startX, int startY, int endX, int endY, float r, float g, float b, float a) {
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(startX, endY, 0).color(r, g, b, a).endVertex();
        builder.pos(endX, endY, 0).color(r, g, b, a).endVertex();
        builder.pos(endX, startY, 0).color(r, g, b, a).endVertex();
        builder.pos(startX, startY, 0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
