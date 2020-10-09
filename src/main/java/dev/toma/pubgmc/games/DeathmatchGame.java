package dev.toma.pubgmc.games;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.data.loadout.Loadout;
import dev.toma.pubgmc.games.args.ArgumentProvider;
import dev.toma.pubgmc.games.interfaces.IObjectManager;
import dev.toma.pubgmc.games.interfaces.IPlayerManager;
import dev.toma.pubgmc.games.interfaces.IZone;
import dev.toma.pubgmc.games.util.Area;
import dev.toma.pubgmc.games.util.GameStorage;
import dev.toma.pubgmc.init.Games;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public final class DeathmatchGame extends Game {

    private final IPlayerManager playerManager;
    private final IObjectManager objectManager;
    private boolean isRunning;
    private int ticksleft;
    private Loadout loadout;

    public DeathmatchGame(World world) {
        super(Games.DEATHMATCH, world);
        this.loadout = Pubgmc.getLoadoutManager().getLoadout(new ResourceLocation("pubgmc:deathmatch"));
        this.playerManager = new DeathmatchPlayerManager(loadout);
        this.objectManager = new IObjectManager.DefaultImpl(this);
        this.addListener(playerManager);
    }

    @Override
    public void onTick() {
        if (--ticksleft <= 0) {
            exec_GameStop();
        }
    }

    @Override
    public void onStop() {
        isRunning = false;
        BlockPos pos = getStorage().getLobby().getLocation();
        for (PlayerEntity player : playerManager.getPlayerList()) {
            player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        }
    }

    @Override
    public void onStart() {
        isRunning = true;
        ticksleft = this.getArgumentValue(ArgumentProvider.DURATION_ARGUMENT);
        List<PlayerEntity> players = playerManager.getPlayerList();
        Area area = getStorage().getArena();
        for (PlayerEntity player : players) {
            BlockPos pos = area.getRandomPosition(world, true);
            player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
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
    public void writeData(CompoundNBT nbt) {
        nbt.putBoolean("running", isRunning);
        nbt.putInt("ticksLeft", ticksleft);
    }

    @Override
    public void readData(CompoundNBT nbt) {
        isRunning = nbt.getBoolean("running");
        ticksleft = nbt.getInt("ticksLeft");
    }

    @Override
    public IZone newZoneInstance(GameStorage storage) {
        return new StaticZone(storage);
    }

    static class DeathmatchPlayerManager extends IPlayerManager.DefaultImpl {

        private final Loadout loadout;

        DeathmatchPlayerManager(Loadout loadout) {
            this.loadout = loadout;
        }

        @Override
        public void gatherPlayers(World world) {
            super.gatherPlayers(world);
            if (loadout != null)
                for (PlayerEntity entity : getPlayerList()) {
                    this.loadout.getRandom(entity.world).processFor(entity);
                }
        }

        @Override
        public void handleLogIn(PlayerEntity entity) {
            super.handleLogIn(entity);
            if (loadout != null)
                this.loadout.getRandom(entity.world).processFor(entity);
        }

        @Override
        public void handlePlayerRespawn(PlayerEntity player, GameStorage storage) {
            super.handlePlayerRespawn(player, storage);
            if (loadout != null)
                this.loadout.getRandom(player.world).processFor(player);
        }
    }
}
