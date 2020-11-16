package dev.toma.pubgmc.internal.animation;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.toma.pubgmc.internal.InternalData;
import dev.toma.pubgmc.util.object.Pair;

import java.util.HashMap;
import java.util.Map;

public interface IContext {

    void apply(float f);

    void update(InternalData.Axis axis, float value);

    boolean isEmpty();

    class Translation implements IContext {

        public final float baseX, baseY, baseZ;
        public float newX, newY, newZ;

        public Translation() {
            this(0.0F, 0.0F, 0.0F);
        }

        public Translation(Translation ctx) {
            this(ctx.baseX + ctx.newX, ctx.baseY + ctx.newY, ctx.baseZ + ctx.newZ);
        }

        public Translation(float baseX, float baseY, float baseZ) {
            this.baseX = baseX;
            this.baseY = baseY;
            this.baseZ = baseZ;
        }

        @Override
        public void update(InternalData.Axis axis, float value) {
            switch (axis) {
                case X: newX += value; break;
                case Y: newY += value; break;
                case Z: newZ += value; break;
            }
        }

        @Override
        public void apply(float smooth) {
            GlStateManager.translatef(baseX + newX * smooth, baseY + newY * smooth, baseZ + newZ * smooth);
        }

        @Override
        public boolean isEmpty() {
            return baseX == 0 && baseY == 0 && baseZ == 0 && newX == 0 && newY == 0 && newZ == 0;
        }
    }

    class Rotation implements IContext {
        public Map<InternalData.Axis, Pair<Float, Float>> rotations = new HashMap<>();

        public Rotation() {

        }

        @SuppressWarnings("SuspiciousNameCombination")
        public Rotation(float rotX, float rotY, float rotZ) {
            if(rotX != 0) rotations.put(InternalData.Axis.X, Pair.of(rotX, 0.0F));
            if(rotY != 0) rotations.put(InternalData.Axis.Y, Pair.of(rotY, 0.0F));
            if(rotZ != 0) rotations.put(InternalData.Axis.Z, Pair.of(rotZ, 0.0F));
        }

        public Rotation(Rotation ctx) {
            for (InternalData.Axis axis : InternalData.Axis.values()) {
                Pair<Float, Float> data = ctx.rotations.get(axis);
                if(data != null) {
                    rotations.put(axis, Pair.of(this.sum(data), 0.0F));
                }
            }
        }

        @Override
        public void update(InternalData.Axis axis, float f) {
            Pair<Float, Float> pair = rotations.computeIfAbsent(axis, k -> Pair.of(0.0F, f));
            pair.setRight(pair.getRight() + f);
            if(pair.getLeft() == 0.0F && pair.getRight() == 0.0F) rotations.remove(axis);
        }

        @Override
        public void apply(float smooth) {
            Pair<Float, Float> v = rotations.get(InternalData.Axis.Y);
            if(v != null) {
                GlStateManager.rotatef(v.getLeft() + v.getRight() * smooth, 0.0F, 1.0F, 0.0F);
            }
            v = rotations.get(InternalData.Axis.X);
            if(v != null) {
                GlStateManager.rotatef(v.getLeft() + v.getRight() * smooth, 1.0F, 0.0F, 0.0F);
            }
            v = rotations.get(InternalData.Axis.Z);
            if(v != null) {
                GlStateManager.rotatef(v.getLeft() + v.getRight() * smooth, 0.0F, 0.0F, 1.0F);
            }
        }

        @Override
        public boolean isEmpty() {
            return rotations.isEmpty();
        }

        private float sum(Pair<Float, Float> pair) {
            return pair.getLeft() + pair.getRight();
        }
    }
}
