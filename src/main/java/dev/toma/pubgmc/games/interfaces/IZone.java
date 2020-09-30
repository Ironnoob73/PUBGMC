package dev.toma.pubgmc.games.interfaces;

import dev.toma.pubgmc.games.Game;
import dev.toma.pubgmc.games.util.Area;
import net.minecraft.util.DamageSource;

import javax.vecmath.Vector2d;

public interface IZone {

    DamageSource ZONE_DAMAGE = new DamageSource("zone").setDamageBypassesArmor().setDamageIsAbsolute();

    boolean isIn(double x, double y);

    Vector2d getMin();

    Vector2d getMax();

    Vector2d getMinLast();

    Vector2d getMaxLast();

    void setSize(Vector2d min, Vector2d max);

    void setSize(Area area);

    void startShrinking(int ticks);

    void tick(Game game);

    boolean isShrinking();

    class Bounds {

        Vector2d min;
        Vector2d max;

        public Bounds(Vector2d min, Vector2d max) {
            this.min = min;
            this.max = max;
        }

        public Vector2d getMin() {
            return min;
        }

        public Vector2d getMax() {
            return max;
        }
    }
}
