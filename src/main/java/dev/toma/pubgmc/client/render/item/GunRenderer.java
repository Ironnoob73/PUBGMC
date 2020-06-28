package dev.toma.pubgmc.client.render.item;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.client.model.gun.AbstractGunModel;
import dev.toma.pubgmc.client.model.gun.P92Model;
import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class GunRenderer extends ItemStackTileEntityRenderer {

    public abstract AbstractGunModel getModel();

    public abstract ResourceLocation getTexture();

    public void offsetModel() {

    }

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        Minecraft.getInstance().getTextureManager().bindTexture(this.getTexture());
        GlStateManager.pushMatrix();
        this.offsetModel();
        GlStateManager.translatef(0.5F, 1.2F, 0.25F);
        GlStateManager.scalef(0.02F, 0.02F, 0.02F);
        GlStateManager.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
        this.getModel().render(itemStackIn);
        GlStateManager.popMatrix();
    }

    protected static ResourceLocation gunResource(String name) {
        return new ResourceLocation(Pubgmc.MODID, "textures/weapons/" + name + ".png");
    }

    public static class P92Renderer extends GunRenderer {

        private final P92Model model = new P92Model();
        private final ResourceLocation texture = gunResource("p92");

        @Override
        public P92Model getModel() {
            return model;
        }

        @Override
        public ResourceLocation getTexture() {
            return texture;
        }
    }
}
