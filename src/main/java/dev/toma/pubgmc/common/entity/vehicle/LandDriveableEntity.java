package dev.toma.pubgmc.common.entity.vehicle;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.init.PMCEntities;
import dev.toma.pubgmc.util.RenderHelper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

public abstract class LandDriveableEntity extends DriveableEntity {

    public boolean isBroken;
    public int timeBeforeBreak;

    public LandDriveableEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public LandDriveableEntity(EntityType<?> type, World world, BlockPos pos, DriveableData data) {
        super(type, world, pos, data);
    }

    @Override
    protected void updateEntityPre() {
        if(this.isInWater()) {
            currentSpeed *= 0.85F;
            if(!isBroken) {
                if(++timeBeforeBreak >= 100) {
                    isBroken = true;
                }
            }
        }
    }

    @Override
    public boolean canAccelerate() {
        return this.hasFuel() && !isBroken;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawOnScreen(Minecraft mc, MainWindow window) {
        int left = 30;
        int top = window.getScaledHeight() - 30;
        Vec3d motion = this.getMotion();
        double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z) * 20;
        String speedString =  speed * 3.6d + " m/s";
        mc.fontRenderer.drawStringWithShadow(speedString, left, top - 8, 0xFFFFFF);
        mc.fontRenderer.drawStringWithShadow("E", left - 7, top + 1, 0xBB0000);
        mc.fontRenderer.drawStringWithShadow("F", left + 152, top + 1, 0x00BB00);
        RenderHelper.drawColoredShape(left, top, left + 150, top + 10, 0.4F, 0.4F, 0.4F, 0.5F);
        float fuel = this.fuel / this.data.getMaxFuel();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.disableTexture();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(left, top + 10, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        builder.pos(left + 150 * fuel, top + 10, 0).color(1.0F - fuel, fuel, 0.0F, 1.0F).endVertex();
        builder.pos(left + 150 * fuel, top, 0).color(1.0F - fuel, fuel, 0.0F, 1.0F).endVertex();
        builder.pos(left, top, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture();
        RenderHelper.drawColoredShape(left, top + 10, left + 150, top + 20, 0.0F, 0.0F, 0.0F, 0.5F);
        float health = this.health / this.data.getMaxHealth();
        RenderHelper.drawColoredShape(left, top + 10, (int)(left + 150 * health), top + 20, 1.0F, health, health, 1.0F);
    }

    public static class UAZDriveable extends LandDriveableEntity {
        public static final DriveableData UAZ_DATA = new DriveableData(100.0F, 150.0F, 1.8F, 0.005F, 0.05F);
        public UAZDriveable(EntityType<?> type, World world) {
            super(type, world);
        }
        public UAZDriveable(World world, BlockPos pos) {
            super(PMCEntities.UAZ, world, pos, UAZ_DATA);
        }
        @Override
        public int maxUserAmount() {
            return 4;
        }
        @Override
        protected double getPassengerX(int index) {
            return index < 2 ? 0.4 : -0.8;
        }
        @Override
        protected double getPassengerY(int index) {
            return 0.3;
        }
        @Override
        protected double getPassengerZ(int index) {
            return index % 2 == 0 ? -0.6 : 0.6;
        }
    }
}
