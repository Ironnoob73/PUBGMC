package dev.toma.pubgmc.client.render.block;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.common.item.healing.HealingItem;
import dev.toma.pubgmc.common.tileentity.LootSpawnerTileEntity;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class LootSpawnerRenderer extends TileEntityRenderer<LootSpawnerTileEntity> {

    public static boolean debugRender = false;

    @Override
    public void render(LootSpawnerTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        if(debugRender) {
            GlStateManager.pushMatrix();
            GlStateManager.translated(x, y, z);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();
            GlStateManager.disableTexture();
            GlStateManager.disableCull();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            renderShape(0.4, -64, 0.4, 0.6, 64, 0.4, builder);
            renderShape(0.6, -64, 0.4, 0.6, 64, 0.6, builder);
            renderShape(0.6, -64, 0.6, 0.4, 64, 0.6, builder);
            renderShape(0.4, -64, 0.6, 0.4, 64, 0.4, builder);
            tessellator.draw();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture();
            GlStateManager.popMatrix();
        }
        BlockPos te = tileEntityIn.getPos();
        Vec3d viewingFrom = Minecraft.getInstance().getRenderViewEntity().getPositionVec();
        double dist = UsefulFunctions.getDistance(te.getX(), te.getY(), te.getZ(), viewingFrom.x, viewingFrom.y + 1, viewingFrom.z);
        if(dist > 20.0D) return;
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        LazyOptional<IItemHandler> inventory = tileEntityIn.getInventory();
        inventory.ifPresent(inv -> {
            GlStateManager.pushMatrix();
            GlStateManager.translated(x + 0.1, y, z + 0.1);
            for(int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if(!stack.isEmpty()) {
                    int ox = i % 3;
                    int oz = i / 3;
                    GlStateManager.pushMatrix();
                    GlStateManager.translatef(ox * 0.45F, 0, oz * 0.45F);
                    GlStateManager.rotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.scaled(0.8, 0.8, 0.8);
                    if(stack.getItem() instanceof GunItem) {
                        GlStateManager.translatef(0.15f, -0.25f, 0.05f);
                        GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
                        GlStateManager.rotatef(-30.0F, 0.0F, 1.0F, 0.0F);
                        GlStateManager.rotatef(90.0F, 0.0F, 0.0F, 1.0F);
                    } else if(stack.getItem() instanceof HealingItem) {
                        GlStateManager.translatef(0, 0, 0.2f);
                        GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
                    }
                    renderer.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
                    GlStateManager.popMatrix();
                }
            }
            GlStateManager.popMatrix();
        });
    }

    private static void renderShape(double x1, double y1, double z1, double x2, double y2, double z2, BufferBuilder builder) {
        builder.pos(x1, y2, z1).color(0.0F, 0.75F, 0.3F, 1.0F).endVertex();
        builder.pos(x2, y2, z2).color(0.0F, 0.75F, 0.3F, 1.0F).endVertex();
        builder.pos(x2, y1, z2).color(0.0F, 0.75F, 0.3F, 1.0F).endVertex();
        builder.pos(x1, y1, z1).color(0.0F, 0.75F, 0.3F, 1.0F).endVertex();
    }
}
