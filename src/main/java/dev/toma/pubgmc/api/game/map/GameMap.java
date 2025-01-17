package dev.toma.pubgmc.api.game.map;

import dev.toma.pubgmc.common.games.playzone.StaticPlayzone;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.Optional;

public interface GameMap extends Area {

    Collection<GameMapPoint> getPoints();

    Optional<GameMapPoint> getPointAt(BlockPos pos);

    void deletePoiAt(BlockPos pos);

    <P extends GameMapPoint> Collection<P> getPoints(GameMapPointType<P> pointType);

    void setMapPoint(BlockPos pos, GameMapPoint point);

    void deletePoints();

    String getMapName();

    StaticPlayzone bounds();

    default boolean isSubMap() {
        return false;
    }
}
