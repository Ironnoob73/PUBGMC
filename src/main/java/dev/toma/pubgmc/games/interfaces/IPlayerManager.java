package dev.toma.pubgmc.games.interfaces;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface IPlayerManager {

    void handleLogIn(PlayerEntity entity);

    void handleLogOut(PlayerEntity entity);

    List<PlayerEntity> getPlayerList();

    void gatherPlayers(World world);

    INBT writeData();

    void readData(World world, INBT nbt);

    class DefaultImpl implements IPlayerManager {

        final List<PlayerEntity> playerList = new ArrayList<>();

        @Override
        public void handleLogIn(PlayerEntity entity) {
            if(!entity.isSpectator()) {
                playerList.add(entity);
            }
        }

        @Override
        public void handleLogOut(PlayerEntity entity) {
            playerList.remove(entity);
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
