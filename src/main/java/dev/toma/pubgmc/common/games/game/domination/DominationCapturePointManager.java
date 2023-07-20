package dev.toma.pubgmc.common.games.game.domination;

import dev.toma.pubgmc.api.game.CaptureZones;
import dev.toma.pubgmc.api.game.map.GameMap;
import dev.toma.pubgmc.api.game.map.GameMapPointType;
import dev.toma.pubgmc.api.game.util.CaptureStatus;
import dev.toma.pubgmc.common.games.map.CaptureZonePoint;
import dev.toma.pubgmc.common.games.map.GameMapPoints;
import dev.toma.pubgmc.common.games.util.TeamType;
import dev.toma.pubgmc.util.helper.GameHelper;
import dev.toma.pubgmc.util.helper.SerializationHelper;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DominationCapturePointManager {

    private final DominationTeamManager teamManager;
    private final float captureSpeed;
    private final Map<BlockPos, Tracker> pointMap = new HashMap<>();
    private static final int NO_OWNER_BG = 0xaaaaaa;
    private static final int BLUE = 0xff;
    private static final int RED = 0xff << 16;

    public DominationCapturePointManager(DominationTeamManager teamManager, float captureSpeed) {
        this.teamManager = teamManager;
        this.captureSpeed = captureSpeed;
    }

    public void init(GameMap map) {
        pointMap.clear();
        map.getPoints(GameMapPoints.CAPTURE_ZONE).forEach(poi -> pointMap.put(poi.getPointPosition(), new Tracker(poi, new CaptureZones.CaptureData(CaptureStatus.NEW, NO_OWNER_BG, 0, 0.0F))));
    }

    @Nullable
    public CaptureZones.CaptureData getCaptureData(BlockPos pos) {
        Tracker tracker = pointMap.get(pos);
        return tracker != null ? tracker.captureData : null;
    }

    public int getCapturedPoints(TeamType type) {
        return (int) pointMap.values().stream().filter(tracker -> tracker.owner == type).count();
    }

    public void update(List<Entity> entityList, World world) {
        boolean updateNeeded = false;
        for (Tracker tracker : pointMap.values()) {
            if (updateCaptureState(tracker, entityList)) {
                updateNeeded = true;
            }
        }
        if (updateNeeded) {
            GameHelper.requestClientGameDataSynchronization(world);
        }
    }

    private boolean updateCaptureState(Tracker tracker, List<Entity> entityList) {
        List<Entity> entitiesOnPoint = entityList.stream().filter(tracker.point::isWithin).collect(Collectors.toList());
        CaptureZones.CaptureData data = tracker.captureData;
        int redCount = getNumberOfPlayers(entitiesOnPoint, TeamType.RED);
        int blueCount = getNumberOfPlayers(entitiesOnPoint, TeamType.BLUE);
        float captureProgress = data.getCaptureProgress();
        data.setStatus(tracker.owner != null ? CaptureStatus.CAPTURED : CaptureStatus.NEW);
        if (captureProgress > 0.0F && blueCount == 0 && redCount == 0) {
            data.setCaptureProgress(captureProgress - this.captureSpeed);
            if (data.getCaptureProgress() <= 0.0F) {
                data.setStatus(tracker.owner != null ? CaptureStatus.CAPTURED : CaptureStatus.NEW);
            }
            return true;
        }
        if (tracker.owner == TeamType.RED) {
            return handleTeamCapture(redCount, blueCount, BLUE, TeamType.BLUE, tracker);
        } else if (tracker.owner == TeamType.BLUE) {
            return handleTeamCapture(blueCount, redCount, RED, TeamType.RED, tracker);
        } else {
            if (redCount > blueCount) {
                handleNewPointCapture(redCount, blueCount, RED, tracker, TeamType.RED);
            } else if (redCount < blueCount) {
                handleNewPointCapture(blueCount, redCount, BLUE, tracker, TeamType.BLUE);
            } else {
                tracker.captureData.setStatus(CaptureStatus.BLOCKED);
            }
            return true;
        }
    }

    private void handleNewPointCapture(int majority, int minority, int majorityColor, Tracker tracker, TeamType majorityTeamType) {
        CaptureZones.CaptureData data = tracker.captureData;
        data.setStatus(CaptureStatus.CAPTURING);
        float progress = data.getCaptureProgress();
        int teamColor = data.getForeground();
        if (progress > 0.0F && teamColor != majorityColor) {
            data.setCaptureProgress(progress - (this.captureSpeed * (majority - minority)));
            if (data.getCaptureProgress() <= 0.0F) {
                data.setForeground(majorityColor);
            }
            return;
        }
        data.setCaptureProgress(progress + (this.captureSpeed * (majority - minority)));
        progress = data.getCaptureProgress();
        if (progress >= 1.0F) {
            if (minority == 0) {
                data.setBackground(majorityColor);
                data.setCaptureProgress(0.0F);
                data.setStatus(CaptureStatus.CAPTURED);
                tracker.owner = majorityTeamType;
            } else {
                data.setStatus(CaptureStatus.BLOCKED);
            }
        }
    }

    private boolean handleTeamCapture(int friendlyCount, int enemyCount, int enemyColor, TeamType enemyTeam, Tracker tracker) {
        CaptureZones.CaptureData data = tracker.captureData;
        if (enemyCount > 0) {
            if (enemyCount > friendlyCount) {
                data.setStatus(CaptureStatus.CAPTURING);
                data.setForeground(enemyColor);
                float newCapProgress = data.getCaptureProgress() + (this.captureSpeed * (enemyCount - friendlyCount));
                data.setCaptureProgress(newCapProgress);
                if (data.getCaptureProgress() == 1.0F) {
                    if (friendlyCount == 0) {
                        data.setStatus(CaptureStatus.CAPTURED);
                        data.setCaptureProgress(0.0F);
                        data.setBackground(enemyColor);
                        tracker.owner = enemyTeam;
                    } else {
                        data.setStatus(CaptureStatus.BLOCKED);
                    }
                }
            } else {
                data.setStatus(CaptureStatus.BLOCKED);
                if (enemyCount != friendlyCount) {
                    data.setCaptureProgress(data.getCaptureProgress() - (this.captureSpeed * (friendlyCount - enemyCount)));
                }
            }
            return true;
        } else if (data.getCaptureProgress() > 0.0F) {
            int power = Math.max(1, friendlyCount);
            data.setCaptureProgress(data.getCaptureProgress() - (this.captureSpeed * power));
            return true;
        }
        return false;
    }

    private int getNumberOfPlayers(List<Entity> entitiesOnPoint, TeamType teamType) {
        return (int) entitiesOnPoint.stream().filter(entity -> {
            TeamType type = teamManager.getTeamType(entity);
            return teamType == type;
        }).count();
    }

    public NBTTagCompound serialize() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("points", SerializationHelper.collectionToNbt(pointMap.values(), Tracker::serialize));
        return nbt;
    }

    public void deserialize(NBTTagCompound nbt) {
        pointMap.clear();
        List<Tracker> list = new ArrayList<>();
        SerializationHelper.collectionFromNbt(list, nbt.getTagList("points", Constants.NBT.TAG_COMPOUND), t -> Tracker.deserialize((NBTTagCompound) t));
        list.forEach(tracker -> pointMap.put(tracker.point.getPointPosition(), tracker));
    }

    private static final class Tracker {

        private final CaptureZonePoint point;
        private final CaptureZones.CaptureData captureData;
        private TeamType owner;

        public Tracker(CaptureZonePoint point, CaptureZones.CaptureData captureData) {
            this.point = point;
            this.captureData = captureData;
        }

        public NBTTagCompound serialize() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("poi", GameMapPointType.serializePointData(point));
            nbt.setTag("data", captureData.serialize());
            if (owner != null) {
                nbt.setInteger("owner", owner.ordinal());
            }
            return nbt;
        }

        public static Tracker deserialize(NBTTagCompound nbt) {
            CaptureZonePoint point = GameMapPointType.deserializePointData(nbt.getCompoundTag("poi"));
            CaptureZones.CaptureData data = new CaptureZones.CaptureData(nbt.getCompoundTag("data"));
            Tracker tracker = new Tracker(point, data);
            if (nbt.hasKey("owner")) {
                tracker.owner = SerializationHelper.enumByIndex(nbt.getInteger("owner"), TeamType.class);
            }
            return tracker;
        }
    }
}