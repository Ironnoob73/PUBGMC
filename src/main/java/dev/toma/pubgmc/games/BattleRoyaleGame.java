package dev.toma.pubgmc.games;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.common.entity.AirdropEntity;
import dev.toma.pubgmc.games.args.ArgumentProvider;
import dev.toma.pubgmc.games.interfaces.IObjectManager;
import dev.toma.pubgmc.games.interfaces.IPlayerManager;
import dev.toma.pubgmc.games.interfaces.IZone;
import dev.toma.pubgmc.games.util.Area;
import dev.toma.pubgmc.games.util.GameStorage;
import dev.toma.pubgmc.games.util.PointOfInterest;
import dev.toma.pubgmc.init.Games;
import dev.toma.pubgmc.init.PMCEntities;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BattleRoyaleGame extends Game {

    private final IPlayerManager playerManager;
    private final IObjectManager objectManager;

    private boolean isRunning;
    private int ticksBeforeShrinking;
    private int airdropDelay;
    private int actualAirdropDelay;
    private Stage stage = Stage.PREPARING;

    private int teamSize;
    private int[] zoneShrinkTimes;
    private int airdropCount;
    private boolean centered;
    private double zoneModifier;

    public BattleRoyaleGame(World world) {
        super(Games.BATTLE_ROYALE, world);
        this.playerManager = new PlayerManager();
        this.objectManager = new IObjectManager.DefaultImpl(this);
        this.addListener(playerManager);
    }

    @Override
    public void onStart() {
        isRunning = true;
        CompoundNBT nbt = new CompoundNBT();
        nbt.putIntArray("array", new int[] {1, 2, 3, 4, 5, 8});
        this.teamSize = this.getArgumentValue(ArgumentProvider.TEAM_SIZE_ARGUMENT);
        this.zoneShrinkTimes = this.getArgumentValue(ArgumentProvider.ZONE_SHRINK_TIMES_ARGUMENT);
        this.airdropCount = this.getArgumentValue(ArgumentProvider.AIRDROP_AMOUNT_ARGUMENT);
        this.centered = this.getArgumentValue(ArgumentProvider.CENTERED_ARGUMENT);
        this.zoneModifier = this.getArgumentValue(ArgumentProvider.ZONE_MODIFIER_ARGUMENT);
        if(airdropCount > 0) {
            int timePool = 0;
            for(int i = 0; i < Math.max(zoneShrinkTimes.length - 2, 0); i++) {
                timePool += zoneShrinkTimes[i];
            }
            if(timePool < airdropCount) {
                Pubgmc.pubgmcLog.warn(marker, "Invalid time schedule for airdrops, no airdrops will spawn");
                airdropCount = 0;
                this.airdropDelay = Integer.MAX_VALUE;
            } else {
                this.airdropDelay = timePool / this.airdropCount;
            }
        }
        actualAirdropDelay = airdropDelay;
        this.ticksBeforeShrinking = 20;
        this.getPlayerManager().getPlayerList().forEach(player -> player.sendStatusMessage(new StringTextComponent("Starting in 10 seconds, get ready!"), true));
    }

    @Override
    public void onTick() {
        ShrinkableZone zone = (ShrinkableZone) this.getZone();
        if(stage.isPlaying()) {
            if(!zone.isShrinking() && !zone.isMaxIndex() && --ticksBeforeShrinking <= 0) {
                if(!zone.isMaxIndex()) {
                    if(--ticksBeforeShrinking <= 0 && !world.isRemote) {
                        int shrinkTime = (int)(zoneShrinkTimes[zone.shrinkIndex] * 0.7);
                        zone.startShrinking(world.rand, shrinkTime, centered, zoneModifier);
                        if(!zone.isMaxIndex()) {
                            ticksBeforeShrinking = zoneShrinkTimes[zone.shrinkIndex];
                        }
                        sync();
                    }
                } else {
                    // wait for people to die and advance to last game stage
                }
            }
            if(!world.isRemote && airdropCount > 0 && --actualAirdropDelay <= 0) {
                spawnAirdrop();
                --airdropCount;
                actualAirdropDelay = airdropDelay;
            }
        } else {
            if(stage.isPreparing()) {
                if(!world.isRemote && --ticksBeforeShrinking <= 0) {
                    stage = stage.advance();
                    ticksBeforeShrinking = zoneShrinkTimes[0];
                    actualAirdropDelay = airdropDelay;
                    GameStorage storage = getStorage();
                    List<PointOfInterest> list = new ArrayList<>(storage.getPois());
                    List<PlayerEntity> players = getPlayerManager().getPlayerList();
                    Area area = storage.getArena();
                    if(list.isEmpty()) {
                        for (PlayerEntity player : players) {
                            BlockPos pos = area.getRandomPosition(world, true);
                            player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                        }
                    } else {
                        Random random = world.rand;
                        list.removeIf(point -> !area.contains(point.getLocation()));
                        for(PlayerEntity player : players) {
                            PointOfInterest poi = list.get(random.nextInt(list.size()));
                            BlockPos pos = getPointAround(poi.getLocation(), area.getRadius());
                            player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                        }
                    }
                    sync();
                }
            } else {
                if(--ticksBeforeShrinking <= 0) {
                    exec_GameStop();
                }
            }
        }
    }

    @Override
    public void onStop() {
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public IZone newZoneInstance(GameStorage storage) {
        return new ShrinkableZone(storage);
    }

    @Override
    public IPlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public IObjectManager getObjectManager() {
        return objectManager;
    }

    @Override
    public boolean createDeathCrate(LivingEntity entity) {
        return true;
    }

    @Override
    public void writeData(CompoundNBT nbt) {
        nbt.putBoolean("isRunning", isRunning);
        nbt.putInt("ticksBeforeShrink", ticksBeforeShrinking);
        nbt.putInt("airdropDelay", airdropDelay);
        nbt.putInt("airdropTicksLeft", actualAirdropDelay);
        nbt.putInt("stage", stage.ordinal());
        nbt.putInt("teamSize", teamSize);
        nbt.putInt("airdropsLeft", airdropCount);
        nbt.putIntArray("zoneTiming", zoneShrinkTimes);
        nbt.putBoolean("centered", centered);
        nbt.putDouble("zoneModifier", zoneModifier);
    }

    @Override
    public void readData(CompoundNBT nbt) {
        isRunning = nbt.getBoolean("isRunning");
        ticksBeforeShrinking = nbt.getInt("ticksBeforeShrink");
        airdropDelay = nbt.getInt("airdropDelay");
        actualAirdropDelay = nbt.getInt("airdropTicksLeft");
        stage = Stage.values()[nbt.getInt("stage")];
        teamSize = nbt.getInt("teamSize");
        airdropCount = nbt.getInt("airdropsLeft");
        zoneShrinkTimes = nbt.getIntArray("zoneTiming");
        centered = nbt.getBoolean("centered");
        zoneModifier = nbt.getDouble("zoneModifier");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderGameOverlay(MainWindow window, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        FontRenderer renderer = mc.fontRenderer;
        renderer.drawString(ticksBeforeShrinking + "", 10, 10, 0xffffff);
    }

    private BlockPos getPointAround(BlockPos center, int maxDistance) {
        Random random = world.rand;
        maxDistance = Math.max(maxDistance, 1);
        int px = (center.getX() - maxDistance) + random.nextInt(2 * maxDistance);
        int pz = (center.getZ() - maxDistance) + random.nextInt(2 * maxDistance);
        int py = world.getHeight(Heightmap.Type.WORLD_SURFACE, px, pz);
        return new BlockPos(px, py, pz);
    }

    private void spawnAirdrop() {
        IZone zone = this.getZone();
        List<PlayerEntity> validPlayers = this.getPlayerManager().getPlayerList().stream().filter(zone::isIn).collect(Collectors.toList());
        if(!validPlayers.isEmpty()) {
            PlayerEntity player = validPlayers.get(world.rand.nextInt(validPlayers.size()));
            BlockPos pos = getPointAround(player.getPosition(), 30);
            AirdropEntity entity = new AirdropEntity(PMCEntities.AIRDROP, world);
            entity.setPosition(pos.getX(), pos.getY() + 100, pos.getZ());
            entity.setGameID(this.getGameID());
            world.addEntity(entity);
        }
    }

    static class PlayerManager extends IPlayerManager.DefaultImpl {

        @Override
        public boolean allowRespawns() {
            return false;
        }

        @Override
        public void handleLogIn(PlayerEntity entity) {
            if(!entity.isSpectator()) {
                entity.setGameType(GameType.SPECTATOR);
            }
            entity.setHealth(entity.getMaxHealth());
            entity.getFoodStats().addStats(20, 20);
        }
    }

    enum Stage {

        PREPARING,
        PLAYING,
        WAITING;

        public boolean isPlaying() {
            return this == PLAYING;
        }

        public boolean isPreparing() {
            return this == PREPARING;
        }

        public Stage advance() {
            int idx = Math.min(ordinal() + 1, values().length - 1);
            return values()[idx];
        }
    }
}
