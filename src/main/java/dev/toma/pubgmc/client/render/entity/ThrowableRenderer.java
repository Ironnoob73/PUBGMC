package dev.toma.pubgmc.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.common.entity.throwable.ThrowableEntity;
import dev.toma.pubgmc.common.item.utility.ThrowableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class ThrowableRenderer<A extends ThrowableEntity, B extends ThrowableItem> extends EntityRenderer<A> {

    private final LazyLoadBase<ItemStack> stackHolder;
    private static final Random rand = new Random(0L);

    public ThrowableRenderer(EntityRendererManager manager, Supplier<B> item) {
        super(manager);
        this.stackHolder = new LazyLoadBase<>(() -> new ItemStack(item.get()));
    }

    @Override
    public void doRender(A entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if(entity.isInvisible()) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        GlStateManager.scalef(0.6f, 0.6f, 0.6f);
        GlStateManager.translatef(-0.5f, 0, -0.5f);
        float rotationProgress = entity.lastRotation + (entity.rotation - entity.lastRotation) * partialTicks;
        GlStateManager.rotatef(rotationProgress, 1.0F, 0.5F, 1.0F);
        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.disableLighting();
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(this.stackHolder.getValue());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
        for(Direction facing : Direction.values()) {
            renderQuads(bufferBuilder, model.getQuads(null, facing, rand));
        }
        renderQuads(bufferBuilder, model.getQuads(null, null, rand));
        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected static void renderQuads(BufferBuilder buffer, List<BakedQuad> quads) {
        int i = 0;
        for (int j = quads.size(); i < j; ++i) {
            BakedQuad quad = quads.get(i);
            LightUtil.renderQuadColor(buffer, quad, -1);
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(A entity) {
        return null;
    }
}
