package dev.toma.pubgmc.games.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class GameStorage implements INBTSerializable<CompoundNBT> {

    private Area lobby = Area.defaultInstance();
    private Area arena = Area.defaultInstance();

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

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("lobby", lobby.serializeNBT());
        nbt.put("arena", arena.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        lobby = new Area(nbt.contains("lobby", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("lobby") : new CompoundNBT());
        arena = new Area(nbt.contains("arena", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("arena") : new CompoundNBT());
    }
}
