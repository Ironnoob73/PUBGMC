package dev.toma.pubgmc.games.interfaces;

import dev.toma.pubgmc.games.util.Area;
import dev.toma.pubgmc.games.util.GameStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface IPlayerManager extends IStateListener {

    void handleLogIn(PlayerEntity entity);

    void handleLogOut(PlayerEntity entity);

    List<PlayerEntity> getPlayerList();

    void gatherPlayers(World world);

    INBT writeData();

    void readData(World world, INBT nbt);

    void handlePlayerDeath(PlayerEntity player, DamageSource source);

    void handlePlayerRespawn(PlayerEntity player, GameStorage storage);

    boolean allowRespawns();

    class DefaultImpl implements IPlayerManager {

        boolean state;
        final List<PlayerEntity> playerList = new ArrayList<>();

        @Override
        public boolean isChanged() {
            return state;
        }

        @Override
        public void markForUpdate() {
            setState(true);
        }

        @Override
        public void clear() {
            setState(false);
        }

        @Override
        public void setState(boolean state) {
            this.state = state;
        }

        @Override
        public void handlePlayerDeath(PlayerEntity player, DamageSource source) {
            playerList.remove(player);
            markForUpdate();
        }

        @Override
        public void handlePlayerRespawn(PlayerEntity player, GameStorage storage) {
            if(this.allowRespawns()) {
                playerList.add(player);
                Area area = storage.getArena();
                BlockPos pos = area.getRandomPosition(player.world, true);
                player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                markForUpdate();
            } else {
                BlockPos pos = storage.getLobby().getLocation();
                player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            }
        }

        @Override
        public boolean allowRespawns() {
            return true;
        }

        @Override
        public void handleLogIn(PlayerEntity entity) {
            if(!entity.isSpectator()) {
                playerList.add(entity);
                markForUpdate();
            }
        }

        @Override
        public void handleLogOut(PlayerEntity entity) {
            playerList.remove(entity);
            markForUpdate();
        }

        @Override
        public List<PlayerEntity> getPlayerList() {
            return playerList;
        }

        @Override
        public void gatherPlayers(World world) {
            playerList.clear();
            for (PlayerEntity playerEntity : world.getPlayers()) {
                if(!playerEntity.isSpectator()) {
                    playerList.add(playerEntity);
                }
            }
            markForUpdate();
        }

        @Override
        public INBT writeData() {
            ListNBT listNBT = new ListNBT();
            for(PlayerEntity entity : playerList) {
                listNBT.add(new StringNBT(entity.getUniqueID().toString()));
            }
            return listNBT;
        }

        @Override
        public void readData(World world, INBT nbt) {
            playerList.clear();
            if(nbt instanceof ListNBT) {
                ListNBT listNBT = (ListNBT) nbt;
                for (INBT inbt : listNBT) {
                    UUID uuid = UUID.fromString(inbt.getString());
                    PlayerEntity playerEntity = world.getPlayerByUuid(uuid);
                    if(playerEntity != null) {
                        playerList.add(playerEntity);
                    }
                }
            }
        }
    }
}
