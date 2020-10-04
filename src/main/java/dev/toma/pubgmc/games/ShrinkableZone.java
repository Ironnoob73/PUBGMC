package dev.toma.pubgmc.games;

import dev.toma.pubgmc.games.interfaces.IZoneRenderer;
import dev.toma.pubgmc.games.util.GameStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.vecmath.Vector2d;
import java.util.Random;

public class ShrinkableZone extends StaticZone {

    protected int shrinkIndex;
    protected boolean shrinking;
    private double sx1, sx2, sz1, sz2;
    private int shrinkTicksLeft;

    private Vector2d nextMin;
    private Vector2d nextMax;

    public ShrinkableZone(GameStorage storage) {
        super(storage);
    }

    @Override
    public void tick(Game game) {
        this.minLast = new Vector2d(min);
        this.maxLast = new Vector2d(max);
        super.tick(game);
        if(shrinkTicksLeft > 0) {
            --shrinkTicksLeft;
            if(shrinkTicksLeft == 0) {
                this.shrinking = false;
                this.min = nextMin;
                this.max = nextMax;
                this.nextMin = null;
                this.nextMax = null;
            } else {
                this.min.add(new Vector2d(sx1, sz1));
                this.max.sub(new Vector2d(sx2, sz2));
            }
        }
    }

    @Override
    public float getZoneDamage() {
        return 0.075F * (float)(Math.pow(2, shrinkIndex));
    }

    @Override
    public void startShrinking(Random random, int time, boolean center, double modifier) {
        if(isSmallestCircle() || shrinking) {
            return;
        }
        shrinking = true;
        double d0 = getMax().x - getMin().x;
        double d1 = d0 / modifier;
        double d2 = 1.0D / time;
        this.shrinkTicksLeft = time;
        if(d1 < 1 || shrinkIndex++ > 6) {
            this.shrinkIndex = Math.min(8, shrinkIndex);
            double c = d0 / 2d;
            double px = getMin().x + c - 0.005;
            double pz = getMin().y + c - 0.005;
            this.nextMin = new Vector2d(px, pz);
            this.nextMax = new Vector2d(px + 0.01, pz + 0.01);
        } else {
            if(center) {
                double r = (d0 - d1) / 2;
                double x1 = min.x + r;
                double z1 = min.y + r;
                double x2 = x1 + d1;
                double z2 = z1 + d1;
                this.nextMin = new Vector2d(x1, z1);
                this.nextMax = new Vector2d(x2, z2);
            } else {
                int l = (int)(d0 - d1);
                double x1 = getMin().x + random.nextInt(l);
                double z1 = getMin().y + random.nextInt(l);
                double x2 = x1 + d1;
                double z2 = z1 + d1;
                this.nextMin = new Vector2d(x1, z1);
                this.nextMax = new Vector2d(x2, z2);
            }
        }
        this.sx1 = Math.abs((min.x - nextMin.x) * d2);
        this.sx2 = Math.abs((nextMax.x - max.x) * d2);
        this.sz1 = Math.abs((min.y - nextMin.y) * d2);
        this.sz2 = Math.abs((nextMax.y - max.y) * d2);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("index", shrinkIndex);
        nbt.putBoolean("shrinking", shrinking);
        if(this.isNextZoneKnown()) {
            nbt.put("nextMin", saveVector(nextMin));
            nbt.put("nextMax", saveVector(nextMax));
        }
        nbt.putDouble("sx1", sx1);
        nbt.putDouble("sx2", sx2);
        nbt.putDouble("sz1", sz1);
        nbt.putDouble("sz2", sz2);
        nbt.putInt("shrinkTicksLeft", shrinkTicksLeft);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        shrinkIndex = nbt.getInt("index");
        shrinking = nbt.getBoolean("shrinking");
        sx1 = nbt.getDouble("sx1");
        sx2 = nbt.getDouble("sx2");
        sz1 = nbt.getDouble("sz1");
        sz2 = nbt.getDouble("sz2");
        shrinkTicksLeft = nbt.getInt("shrinkTicksLeft");
        if(nbt.contains("nextMin", Constants.NBT.TAG_COMPOUND) && nbt.contains("nextMax", Constants.NBT.TAG_COMPOUND)) {
            nextMin = loadVector(nbt.getCompound("nextMin"));
            nextMax = loadVector(nbt.getCompound("nextMax"));
        }
    }

    @Override
    public boolean isShrinking() {
        return shrinking;
    }

    public boolean isMaxIndex() {
        return shrinkIndex == 8;
    }

    private boolean isSmallestCircle() {
        return !isShrinking() && getMin().equals(getMax());
    }

    private boolean isNextZoneKnown() {
        return nextMin != null && nextMax != null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IZoneRenderer getRenderer() {
        return IZoneRenderer.MOVING;
    }
}
