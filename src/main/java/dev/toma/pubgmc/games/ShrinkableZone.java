package dev.toma.pubgmc.games;

import dev.toma.pubgmc.games.util.GameStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import javax.vecmath.Vector2d;
import java.util.Random;

public class ShrinkableZone extends StaticZone {

    protected final float pct;
    private int shrinkIndex;
    private boolean shrinking;

    private Vector2d nextMin;
    private Vector2d nextMax;

    public ShrinkableZone(GameStorage storage) {
        super(storage);
        this.pct = Math.abs((2250.0F - (storage.getArena().getRadius() * 2)) / 2250.0F);
    }

    @Override
    public void startShrinking(Random random, int time) {
        if(isSmallestCircle() || shrinking) {
            return;
        }
        shrinking = true;
        double d = getMax().x - getMin().x;
        double d1 = d * pct;
        if(d1 < 20 || shrinkIndex++ > 7) {
            double c = d / 2d;
            double px = getMin().x + c;
            double pz = getMin().y + c;
            this.nextMin = new Vector2d(px, pz);
            this.nextMax = new Vector2d(nextMin);
        } else {
            int l = (int)(d - d1);
            double x1 = getMin().x + random.nextInt(l);
            double z1 = getMin().y + random.nextInt(l);
            double x2 = x1 + d1;
            double z2 = z1 + d1;
            this.nextMin = new Vector2d(x1, z1);
            this.nextMax = new Vector2d(x2, z2);
        }
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
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        shrinkIndex = nbt.getInt("index");
        shrinking = nbt.getBoolean("shrinking");
        if(nbt.contains("nextMin", Constants.NBT.TAG_COMPOUND) && nbt.contains("nextMax", Constants.NBT.TAG_COMPOUND)) {
            nextMin = loadVector(nbt.getCompound("nextMin"));
            nextMax = loadVector(nbt.getCompound("nextMax"));
        }
    }

    @Override
    public boolean isShrinking() {
        return shrinking;
    }

    private boolean isSmallestCircle() {
        return !isShrinking() && getMin().equals(getMax());
    }

    private boolean isNextZoneKnown() {
        return nextMin != null && nextMax != null;
    }
}
