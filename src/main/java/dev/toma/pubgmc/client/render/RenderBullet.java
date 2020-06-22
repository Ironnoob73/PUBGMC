package dev.toma.pubgmc.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.common.entity.BulletEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderBullet extends EntityRenderer<BulletEntity> {

    public RenderBullet(EntityRendererManager manager) {
        super(manager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(BulletEntity bulletEntity) {
        return null;
    }

    @Override
    public void doRender(BulletEntity entity, double x, double y, double z, float yaw, float partialTicks) {
        entity.origin.ifPresent(vec -> {
            GlStateManager.disableTexture();
            GlStateManager.disableCull();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();
            builder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            builder.pos(vec.x, vec.y, vec.z).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
            builder.pos(x, y, z).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
            tessellator.draw();
            GlStateManager.enableCull();
            GlStateManager.enableTexture();
        });
    }
}
