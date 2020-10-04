package dev.toma.pubgmc.games;

import dev.toma.pubgmc.games.interfaces.IPlayerManager;
import dev.toma.pubgmc.games.interfaces.IZone;
import dev.toma.pubgmc.games.interfaces.IZoneRenderer;
import dev.toma.pubgmc.games.util.Area;
import dev.toma.pubgmc.games.util.GameStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.vecmath.Vector2d;
import java.util.Random;

public class StaticZone implements IZone {

    protected Vector2d min;
    protected Vector2d max;
    protected Vector2d minLast;
    protected Vector2d maxLast;

    public StaticZone(GameStorage storage) {
        this.setInitialSize(storage);
    }

    public void setInitialSize(GameStorage storage) {
        setSize(storage.getArena());
    }

    @Override
    public Vector2d getMin() {
        return min;
    }

    @Override
    public Vector2d getMax() {
        return max;
    }

    @Override
    public Vector2d getMinLast() {
        return minLast;
    }

    @Override
    public Vector2d getMaxLast() {
        return maxLast;
    }

    @Override
    public void startShrinking(Random random, int time, boolean center, double modifier) {

    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public IZoneRenderer getRenderer() {
        return IZoneRenderer.BASIC;
    }

    @Override
    public void tick(Game game) {
        IPlayerManager manager = game.getPlayerManager();
        long time = game.world.getGameTime();
        if(time % 30L == 0L) {
            for (PlayerEntity player : manager.getPlayerList()) {
                if(!isIn(player))
                    player.attackEntityFrom(ZONE_DAMAGE, this.getZoneDamage());
            }
        }
    }

    public float getZoneDamage() {
        return 4.0F;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("min", saveVector(min));
        nbt.put("max", saveVector(max));
        nbt.put("minLast", saveVector(minLast));
        nbt.put("maxLast", saveVector(maxLast));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        min = loadVector(nbt.getCompound("min"));
        max = loadVector(nbt.getCompound("max"));
        minLast = loadVector(nbt.getCompound("minLast"));
        maxLast = loadVector(nbt.getCompound("maxLast"));
    }

    public CompoundNBT saveVector(Vector2d vector2d) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putDouble("x", vector2d.x);
        nbt.putDouble("y", vector2d.y);
        return nbt;
    }

    public Vector2d loadVector(CompoundNBT nbt) {
        return new Vector2d(nbt.getDouble("x"), nbt.getDouble("y"));
    }

    @Override
    public boolean isShrinking() {
        return false;
    }

    @Override
    public void setSize(Vector2d min, Vector2d max) {
        this.minLast = min;
        this.maxLast = max;
        this.min = min;
        this.max = max;
    }

    @Override
    public void setSize(Area area) {
        double x = area.getLocation().getX() + 0.5;
        double z = area.getLocation().getZ() + 0.5;
        int size = area.getRadius();
        setSize(new Vector2d(x - size, z - size), new Vector2d(x + size, z + size));
    }

    @Override
    public boolean isIn(double x, double z) {
        return x > min.x && x < max.x && z > min.y && z < max.y;
    }

    @Override
    public boolean isIn(Entity entity) {
        return isIn(entity.posX, entity.posZ);
    }
}
