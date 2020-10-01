package dev.toma.pubgmc.games;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.games.args.ArgumentProvider;
import dev.toma.pubgmc.games.interfaces.IObjectManager;
import dev.toma.pubgmc.games.interfaces.IPlayerManager;
import dev.toma.pubgmc.games.interfaces.IZone;
import dev.toma.pubgmc.games.util.GameStorage;
import dev.toma.pubgmc.init.Games;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

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

    public BattleRoyaleGame(World world) {
        super(Games.BATTLE_ROYALE, world);
        this.playerManager = new PlayerManager();
        this.objectManager = new IObjectManager.DefaultImpl(this);
        this.addListener(playerManager);
    }

    @Override
    public void onStart() {
        isRunning = true;
        this.teamSize = this.getArgumentValue(ArgumentProvider.TEAM_SIZE_ARGUMENT);
        this.zoneShrinkTimes = this.getArgumentValue(ArgumentProvider.ZONE_SHRINK_TIMES_ARGUMENT);
        this.airdropCount = this.getArgumentValue(ArgumentProvider.AIRDROP_AMOUNT_ARGUMENT);
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
        this.ticksBeforeShrinking = 200;
        this.getPlayerManager().getPlayerList().forEach(player -> player.sendStatusMessage(new StringTextComponent("Starting in 10 seconds, get ready!"), true));
    }

    @Override
    public void onTick() {
        ShrinkableZone zone = (ShrinkableZone) this.getZone();
        if(stage.isPlaying()) {
            if(!zone.isShrinking() && !zone.isMaxIndex() && --ticksBeforeShrinking <= 0) {
                int shrinkTime = (int)(zoneShrinkTimes[zone.shrinkIndex] * 0.7);
                zone.startShrinking(world.rand, shrinkTime);
                if(!zone.isMaxIndex()) {
                    ticksBeforeShrinking = zoneShrinkTimes[zone.shrinkIndex];
                }
            }
            if(airdropCount > 0 && --airdropDelay <= 0) {
                // TODO add airdrop
                --airdropCount;
                airdropDelay = actualAirdropDelay;
            }
        } else {
            if(stage.isPreparing()) {
                if(--ticksBeforeShrinking <= 0) {
                    stage = stage.advance();
                    ticksBeforeShrinking = zoneShrinkTimes[0];
                    airdropDelay = actualAirdropDelay;
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
        return null;
    }

    @Override
    public IObjectManager getObjectManager() {
        return null;
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
