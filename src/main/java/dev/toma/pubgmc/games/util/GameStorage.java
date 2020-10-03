package dev.toma.pubgmc.games.util;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.games.Game;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class GameStorage implements INBTSerializable<CompoundNBT> {

    private Area lobby = Area.defaultInstance();
    private Area arena = Area.defaultInstance();
    private List<PointOfInterest> pois = new ArrayList<>();

    public Area getLobby() {
        return lobby;
    }

    public void setLobby(Area lobby) {
        this.lobby = lobby;
    }

    public Area getArena() {
        return arena;
    }

    public void setArena(Area arena) {
        this.arena = arena;
    }

    public void addPoI(PointOfInterest PoI) {
        pois.add(PoI);
    }

    public void removePoI(int index) {
        pois.remove(index);
    }

    public List<PointOfInterest> getPois() {
        return pois;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("lobby", lobby.serializeNBT());
        nbt.put("arena", arena.serializeNBT());
        ListNBT poiNBT = new ListNBT();
        for(PointOfInterest point : pois) {
            poiNBT.add(point.serializeNBT());
        }
        nbt.put("pois", poiNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        pois.clear();
        lobby = new Area(nbt.contains("lobby", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("lobby") : new CompoundNBT());
        arena = new Area(nbt.contains("arena", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("arena") : new CompoundNBT());
        if(nbt.contains("pois", Constants.NBT.TAG_LIST)) {
            ListNBT poiNBT = nbt.getList("pois", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < poiNBT.size(); i++) {
                CompoundNBT compoundNBT = poiNBT.getCompound(i);
                try {
                    addPoI(new PointOfInterest(compoundNBT));
                } catch (RuntimeException ex) {
                    Pubgmc.pubgmcLog.warn(Game.marker, "Exception loading saved PointOfInterest from NBT: {}", ex.toString());
                }
            }
        }
    }
}
