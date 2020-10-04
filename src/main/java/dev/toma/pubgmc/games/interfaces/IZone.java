package dev.toma.pubgmc.games.interfaces;

import dev.toma.pubgmc.games.Game;
import dev.toma.pubgmc.games.util.Area;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;

import javax.vecmath.Vector2d;
import java.util.Random;

public interface IZone extends INBTSerializable<CompoundNBT> {

    DamageSource ZONE_DAMAGE = new DamageSource("zone").setDamageBypassesArmor().setDamageIsAbsolute();

    boolean isIn(double x, double y);

    boolean isIn(Entity entity);

    Vector2d getMin();

    Vector2d getMax();

    Vector2d getMinLast();

    Vector2d getMaxLast();

    void setSize(Vector2d min, Vector2d max);

    void setSize(Area area);

    void startShrinking(Random random, int time, boolean center, double modifier);

    void tick(Game game);

    boolean isShrinking();

    float getZoneDamage();

    @OnlyIn(Dist.CLIENT)
    IZoneRenderer getRenderer();
}
