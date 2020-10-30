package dev.toma.pubgmc.client.screen.util;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.model.gun.attachment.AttachmentModel;
import dev.toma.pubgmc.client.render.item.GunRenderer;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AttachmentSettings {

    private static final AttachmentSettings INSTANCE = new AttachmentSettings();
    public static final int RED_DOT = 0;
    public static final int HOLO = 1;
    public static final int X2 = 2;
    public static final int X3 = 3;
    public static final int X4 = 4;
    public static final int X6 = 5;
    public static final int X8 = 6;
    public static final int X15 = 7;
    public static final int SUPPRESSOR_SMG = 8;
    public static final int SUPPRESSOR_AR = 9;
    public static final int SUPPRESSOR_SR = 10;
    public static final int VERTICAL_GRIP = 11;
    public static final int ANGLED_GRIP = 12;
    final Map<Integer, ModelSettings> settingsMap = new HashMap<>();

    AttachmentSettings() {
        settingsMap.put(RED_DOT, new ModelSettings("Red Dot", () -> GunRenderer.RED_DOT));
        settingsMap.put(X15, new ModelSettings("15x", () -> GunRenderer.SCOPE_15X));
        settingsMap.put(SUPPRESSOR_SMG, new ModelSettings("Suppressor (SMG)", () -> GunRenderer.PISTOL_SUPPRESSOR));
        settingsMap.put(VERTICAL_GRIP, new ModelSettings("Vertical grip", () -> GunRenderer.VERTICAL_GRIP));
    }

    public static AttachmentSettings instance() {
        return INSTANCE;
    }

    public static class ModelSettings {
        private final String name;
        private final Supplier<AttachmentModel> modelReference;
        private boolean enabled = false;
        private Vec3d position = Vec3d.ZERO;
        private Vec3d scale = new Vec3d(1.0, 1.0, 1.0);
        private double rotX;
        private double rotY;
        private double rotZ;

        ModelSettings(String name, Supplier<AttachmentModel> supplier) {
            this.name = name;
            this.modelReference = supplier;
        }

        public String getName() {
            return name;
        }

        public void render() {
            if(!enabled)
                return;
            GlStateManager.pushMatrix();
            GlStateManager.translated(position.x, position.y, position.z);
            GlStateManager.scaled(scale.x, scale.y, scale.z);
            if(rotX != 0)
                GlStateManager.rotated(rotX, 1.0, 0.0, 0.0);
            if(rotY != 0)
                GlStateManager.rotated(rotY, 0.0, 1.0, 0.0);
            if(rotZ != 0)
                GlStateManager.rotated(rotZ, 0.0, 0.0, 1.0);
            this.modelReference.get().doRender();
            GlStateManager.popMatrix();
        }

        public void reset() {
            this.position = Vec3d.ZERO;
            this.scale = new Vec3d(1.0, 1.0, 1.0);
            this.rotX = 0;
            this.rotY = 0;
            this.rotZ = 0;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setPosition(Vec3d pos) {
            this.position = pos;
        }

        public void setScale(Vec3d scale) {
            this.scale = scale;
        }

        public void setRotX(double rotX) {
            this.rotX = rotX;
        }

        public void setRotY(double rotY) {
            this.rotY = rotY;
        }

        public void setRotZ(double rotZ) {
            this.rotZ = rotZ;
        }
    }
}
