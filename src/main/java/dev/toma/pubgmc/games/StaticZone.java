package dev.toma.pubgmc.games;

import dev.toma.pubgmc.games.interfaces.IPlayerManager;
import dev.toma.pubgmc.games.interfaces.IZone;
import dev.toma.pubgmc.games.util.Area;
import dev.toma.pubgmc.games.util.GameStorage;
import net.minecraft.entity.player.PlayerEntity;

import javax.vecmath.Vector2d;

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
    public void startShrinking(int ticks) {

    }

    @Override
    public void tick(Game game) {
        IPlayerManager manager = game.getPlayerManager();
        long time = game.world.getGameTime();
        if(time % 30L == 0L) {
            for (PlayerEntity player : manager.getPlayerList()) {
                player.attackEntityFrom(ZONE_DAMAGE, 4.0F);
            }
        }
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
        double x = area.getLocation().getX();
        double z = area.getLocation().getZ();
        int size = area.getRadius();
        setSize(new Vector2d(x - size, z - size), new Vector2d(x + size, z + size));
    }

    @Override
    public boolean isIn(double x, double z) {
        return x > min.x && x < max.x && z > min.y && z < max.y;
    }
}
