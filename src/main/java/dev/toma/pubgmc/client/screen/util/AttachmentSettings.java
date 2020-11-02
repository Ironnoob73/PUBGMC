package dev.toma.pubgmc.client.screen.util;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.client.model.gun.attachment.AttachmentModel;
import dev.toma.pubgmc.client.render.item.GunRenderer;

import javax.vecmath.Vector3f;
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
        settingsMap.put(RED_DOT, new ModelSettings("Red Dot", () -> GunRenderer.RED_DOT, "RED_DOT", "hasRedDot"));
        settingsMap.put(HOLO, new ModelSettings("Holographic", () -> GunRenderer.HOLO, "HOLO", "hasHolographic"));
        settingsMap.put(X2, new ModelSettings("2x", () -> GunRenderer.SCOPE_2X, "SCOPE_2X", "has2x"));
        settingsMap.put(X4, new ModelSettings("4x", () -> GunRenderer.SCOPE_4X, "SCOPE_4X", "has4x"));
        settingsMap.put(X8, new ModelSettings("8x", () -> GunRenderer.SCOPE_8X, "SCOPE_8X", "has8x"));
        settingsMap.put(X15, new ModelSettings("15x", () -> GunRenderer.SCOPE_15X, "SCOPE_15X", "has15x"));
        settingsMap.put(SUPPRESSOR_SMG, new ModelSettings("Suppressor (SMG)", () -> GunRenderer.SMG_SUPPRESSOR, "SMG_SUPPRESSOR", "hasSilencer"));
        settingsMap.put(SUPPRESSOR_AR, new ModelSettings("Suppressor (AR)", () -> GunRenderer.AR_SUPPRESSOR, "AR_SUPPRESSOR", "hasSilencer"));
        settingsMap.put(SUPPRESSOR_SR, new ModelSettings("Suppressor (SR)", () -> GunRenderer.SR_SUPPRESSOR, "SR_SUPPRESSOR", "hasSilencer"));
        settingsMap.put(VERTICAL_GRIP, new ModelSettings("Vertical grip", () -> GunRenderer.VERTICAL_GRIP, "VERTICAL_GRIP", "hasVerticalGrip"));
        settingsMap.put(ANGLED_GRIP, new ModelSettings("Angled grip", () -> GunRenderer.ANGLED_GRIP, "ANGLED_GRIP", "hasAngledGrip"));
    }

    public static AttachmentSettings instance() {
        return INSTANCE;
    }

    public void renderAll() {
        for (ModelSettings settings : settingsMap.values()) {
            if(settings.isEnabled()) {
                settings.render();
            }
        }
    }

    public static class ModelSettings {
        private final String name;
        private final String referenceName;
        private final String functionName;
        private final Supplier<AttachmentModel> modelReference;
        private boolean enabled = false;
        private Vector3f position = new Vector3f();
        private Vector3f scale = new Vector3f(1.0F, 1.0F, 1.0F);
        private float rotX;
        private float rotY;
        private float rotZ;

        ModelSettings(String name, Supplier<AttachmentModel> supplier, String referenceName, String functionName) {
            this.name = name;
            this.modelReference = supplier;
            this.referenceName = referenceName;
            this.functionName = functionName;
        }

        public String getName() {
            return name;
        }

        public void render() {
            if(!enabled)
                return;
            GlStateManager.pushMatrix();
            GlStateManager.translatef(position.x, position.y, position.z);
            GlStateManager.scalef(scale.x, scale.y, scale.z);
            if(rotX != 0)
                GlStateManager.rotatef(rotX, 1.0F, 0.0F, 0.0F);
            if(rotY != 0)
                GlStateManager.rotatef(rotY, 0.0F, 1.0F, 0.0F);
            if(rotZ != 0)
                GlStateManager.rotatef(rotZ, 0.0F, 0.0F, 1.0F);
            this.modelReference.get().doRender();
            GlStateManager.popMatrix();
        }

        public void reset() {
            this.position = new Vector3f();
            this.scale = new Vector3f(1.0F, 1.0F, 1.0F);
            this.rotX = 0;
            this.rotY = 0;
            this.rotZ = 0;
        }

        public boolean isModified() {
            return !position.equals(new Vector3f()) || !scale.equals(new Vector3f(1.0F, 1.0F, 1.0F)) || rotX != 0 || rotY != 0 || rotZ != 0;
        }

        public String getString() {
            StringBuilder builder = new StringBuilder();
            builder.append("if(AttachmentHelper.").append(functionName).append("(item, stack)) {\n");
            builder.append("GlStateManager.pushMatrix();\n");
            if(!position.equals(new Vector3f())) {
                builder.append("GlStateManager.translatef(").append(position.x).append("F, ").append(position.y).append("F, ").append(position.z).append("F);\n");
            }
            if(!scale.equals(new Vector3f(1.0F, 1.0F, 1.0F))) {
                builder.append("GlStateManager.scalef(").append(scale.x).append("F, ").append(scale.y).append("F, ").append(scale.z).append("F);\n");
            }
            if(rotX != 0)
                builder.append("GlStateManager.rotatef(").append(rotX).append("F, 1.0F, 0.0F, 0.0F);\n");
            if(rotY != 0)
                builder.append("GlStateManager.rotatef(").append(rotY).append("F, 0.0F, 1.0F, 0.0F);\n");
            if(rotZ != 0)
                builder.append("GlStateManager.rotatef(").append(rotZ).append("F, 0.0F, 0.0F, 1.0F);\n");
            builder.append(referenceName).append(".doRender();\n");
            builder.append("GlStateManager.popMatrix();\n");
            builder.append("}\n");
            return builder.toString();
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setPosX(float x) {
            this.position.x = x;
        }

        public void setPosY(float y) {
            this.position.y = y;
        }

        public void setPosZ(float z) {
            this.position.z = z;
        }

        public void setScaleX(float x) {
            this.scale.x = x;
        }

        public void setScaleY(float y) {
            this.scale.y = y;
        }

        public void setScaleZ(float z) {
            this.scale.z = z;
        }

        public void setRotX(float rotX) {
            this.rotX = rotX;
        }

        public void setRotY(float rotY) {
            this.rotY = rotY;
        }

        public void setRotZ(float rotZ) {
            this.rotZ = rotZ;
        }

        public float getPosX() {
            return position.x;
        }

        public float getPosY() {
            return position.y;
        }

        public float getPosZ() {
            return position.z;
        }

        public float getScaleX() {
            return scale.x;
        }

        public float getScaleY() {
            return scale.y;
        }

        public float getScaleZ() {
            return scale.z;
        }

        public float getRotX() {
            return rotX;
        }

        public float getRotY() {
            return rotY;
        }

        public float getRotZ() {
            return rotZ;
        }
    }
}
