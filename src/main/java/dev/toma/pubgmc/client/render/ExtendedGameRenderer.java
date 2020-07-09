package dev.toma.pubgmc.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.ClientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.MathHelper;

public class ExtendedGameRenderer extends GameRenderer {

    public ExtendedGameRenderer(Minecraft mc, IResourceManager resourceManager) {
        super(mc, resourceManager);
    }

    @Override
    public void updateCameraAndRender(float partialTicks, long nanoTime) {
        Minecraft mc = Minecraft.getInstance();
        WorldRenderer worldrenderer = mc.worldRenderer;
        ParticleManager particlemanager = mc.particles;
        boolean flag = this.isDrawBlockOutline();
        GlStateManager.enableCull();
        mc.getProfiler().endStartSection("camera");
        this.setupCameraTransform(partialTicks);
        ActiveRenderInfo activerenderinfo = this.activeRender;
        activerenderinfo.update(mc.world, mc.getRenderViewEntity() == null ? mc.player : mc.getRenderViewEntity(), mc.gameSettings.thirdPersonView > 0, mc.gameSettings.thirdPersonView == 2, partialTicks);
        ClippingHelper clippinghelper = ClippingHelperImpl.getInstance();
        worldrenderer.func_224745_a(activerenderinfo);
        mc.getProfiler().endStartSection("clear");
        GlStateManager.viewport(0, 0, mc.mainWindow.getFramebufferWidth(), mc.mainWindow.getFramebufferHeight());
        this.fogRenderer.updateFogColor(activerenderinfo, partialTicks);
        GlStateManager.clear(16640, Minecraft.IS_RUNNING_ON_MAC);
        mc.getProfiler().endStartSection("culling");
        ICamera icamera = new Frustum(clippinghelper);
        double d0 = activerenderinfo.getProjectedView().x;
        double d1 = activerenderinfo.getProjectedView().y;
        double d2 = activerenderinfo.getProjectedView().z;
        icamera.setPosition(d0, d1, d2);
        if (mc.gameSettings.renderDistanceChunks >= 4) {
            this.fogRenderer.setupFog(activerenderinfo, -1, partialTicks);
            mc.getProfiler().endStartSection("sky");
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.multMatrix(Matrix4f.perspective(this.getFOVModifier(activerenderinfo, partialTicks, true), (float) mc.mainWindow.getFramebufferWidth() / (float) mc.mainWindow.getFramebufferHeight(), 0.05F, this.farPlaneDistance * 2.0F));
            GlStateManager.matrixMode(5888);
            worldrenderer.renderSky(partialTicks);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.multMatrix(Matrix4f.perspective(this.getFOVModifier(activerenderinfo, partialTicks, true), (float) mc.mainWindow.getFramebufferWidth() / (float) mc.mainWindow.getFramebufferHeight(), 0.05F, this.farPlaneDistance * MathHelper.SQRT_2));
            GlStateManager.matrixMode(5888);
        }
        this.fogRenderer.setupFog(activerenderinfo, 0, partialTicks);
        GlStateManager.shadeModel(7425);
        if (activerenderinfo.getProjectedView().y < 128.0D) {
            this.renderClouds(activerenderinfo, worldrenderer, partialTicks, d0, d1, d2);
        }
        mc.getProfiler().endStartSection("prepareterrain");
        this.fogRenderer.setupFog(activerenderinfo, 0, partialTicks);
        mc.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        mc.getProfiler().endStartSection("terrain_setup");
        mc.world.getChunkProvider().func_212863_j_().tick(Integer.MAX_VALUE, true, true);
        if(!ClientManager.isRenderingPiP) worldrenderer.setupTerrain(activerenderinfo, icamera, this.frameCount++, mc.player.isSpectator());
        mc.getProfiler().endStartSection("updatechunks");
        mc.worldRenderer.updateChunks(nanoTime);
        mc.getProfiler().endStartSection("terrain");
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.disableAlphaTest();
        worldrenderer.renderBlockLayer(BlockRenderLayer.SOLID, activerenderinfo);
        GlStateManager.enableAlphaTest();
        mc.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, mc.gameSettings.mipmapLevels > 0); // FORGE: fix flickering leaves when mods mess up the blurMipmap settings
        worldrenderer.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, activerenderinfo);
        mc.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        mc.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        worldrenderer.renderBlockLayer(BlockRenderLayer.CUTOUT, activerenderinfo);
        mc.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.shadeModel(7424);
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        mc.getProfiler().endStartSection("entities");
        worldrenderer.renderEntities(activerenderinfo, icamera, partialTicks);
        RenderHelper.disableStandardItemLighting();
        this.disableLightmap();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        if (flag && mc.objectMouseOver != null) {
            GlStateManager.disableAlphaTest();
            mc.getProfiler().endStartSection("outline");
            if (!net.minecraftforge.client.ForgeHooksClient.onDrawBlockHighlight(worldrenderer, activerenderinfo, mc.objectMouseOver, 0, partialTicks))
                worldrenderer.drawSelectionBox(activerenderinfo, mc.objectMouseOver, 0);
            GlStateManager.enableAlphaTest();
        }
        if (mc.debugRenderer.shouldRender()) {
            mc.debugRenderer.renderDebug(nanoTime);
        }
        mc.getProfiler().endStartSection("destroyProgress");
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        mc.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        worldrenderer.func_215318_a(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), activerenderinfo);
        mc.getTextureManager().getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.disableBlend();
        this.enableLightmap();
        this.fogRenderer.setupFog(activerenderinfo, 0, partialTicks);
        mc.getProfiler().endStartSection("particles");
        particlemanager.renderParticles(activerenderinfo, partialTicks);
        this.disableLightmap();
        GlStateManager.depthMask(false);
        GlStateManager.enableCull();
        mc.getProfiler().endStartSection("weather");
        this.renderRainSnow(partialTicks);
        GlStateManager.depthMask(true);
        worldrenderer.renderWorldBorder(activerenderinfo, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(516, 0.1F);
        this.fogRenderer.setupFog(activerenderinfo, 0, partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        mc.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.shadeModel(7425);
        mc.getProfiler().endStartSection("translucent");
        worldrenderer.renderBlockLayer(BlockRenderLayer.TRANSLUCENT, activerenderinfo);
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableFog();
        if (activerenderinfo.getProjectedView().y >= 128.0D) {
            mc.getProfiler().endStartSection("aboveClouds");
            this.renderClouds(activerenderinfo, worldrenderer, partialTicks, d0, d1, d2);
        }
        mc.getProfiler().endStartSection("forge_render_last");
        net.minecraftforge.client.ForgeHooksClient.dispatchRenderLast(worldrenderer, partialTicks);
        mc.getProfiler().endStartSection("hand");
        if (this.renderHand && !ClientManager.isRenderingPiP) {
            GlStateManager.clear(256, Minecraft.IS_RUNNING_ON_MAC);
            this.renderHand(activerenderinfo, partialTicks);
        }
    }
}
