package dev.toma.pubgmc.games;

import com.google.common.collect.Sets;
import dev.toma.pubgmc.capability.IWorldCap;
import dev.toma.pubgmc.capability.world.WorldDataFactory;
import dev.toma.pubgmc.games.interfaces.*;
import dev.toma.pubgmc.games.util.GameStorage;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketSyncWorldData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Objects;
import java.util.Random;
import java.util.Set;

public abstract class Game implements INBTSerializable<CompoundNBT>, IKeyHolder, GameManager {

    public final World world;
    protected final IWorldCap worldData;
    protected final GameType<?> type;
    private final Set<IStateListener> stateListeners = Sets.newHashSet();
    private long gameID;

    public Game(GameType<?> type, World world) {
        this.type = Objects.requireNonNull(type, "Game type must be nonnull");
        this.world = Objects.requireNonNull(world, "Game's world must be nonnull");
        this.worldData = WorldDataFactory.getData(world);
    }

    /* =============================================================================================================================== */

    // Game lifecycle functions. Should not be called randomly

    public final void exec_GameStart(CompoundNBT arguments) {
        IPlayerManager manager = this.getPlayerManager();
        manager.gatherPlayers(world);
        if(this instanceof IDataHolder<?>) {
            ((IDataHolder<?>) this).resetData();
        }
        GameType<?> type = this.getType();
        if(type.hasArguments()) {
            this.getType().getGameArguments().updateValues(arguments);
        }
        this.onStart();
    }

    public final void exec_GameStop() {
        if(this instanceof IDataHolder<?>) {
            ((IDataHolder<?>) this).resetData();
        }
        this.onStop();
    }

    public final void exec_GameTick() {
        boolean sync = false;
        for (IStateListener listener : stateListeners) {
            if(listener.isChanged()) {
                listener.clear();
                sync = true;
            }
        }
        if(sync) {
            sync();
        }
        this.onTick();
    }

    public final void addListener(IStateListener listener) {
        this.stateListeners.add(listener);
    }

    public final void sync() {
        if(!world.isRemote) {
            CompoundNBT nbt = serializeNBT();
            NetworkManager.sendToAll(world, new CPacketSyncWorldData(nbt));
        }
    }

    /* =============================================================================================================================== */

    public abstract void onStart();

    public abstract void onStop();

    public abstract void onTick();

    public abstract boolean isRunning();

    public GameType<?> getType() {
        return type;
    }

    @Override
    public long getGameID() {
        return gameID;
    }

    @Override
    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    public void setNewGameID() {
        this.setGameID(createGameID());
    }

    public void writeData(CompoundNBT nbt) {

    }

    public void readData(CompoundNBT nbt) {

    }

    @Override
    public final CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("gameID", gameID);
        nbt.put("players", getPlayerManager().writeData());
        writeData(nbt);
        return nbt;
    }

    @Override
    public final void deserializeNBT(CompoundNBT nbt) {
        gameID = nbt.getLong("gameID");
        if(nbt.contains("players")) {
            getPlayerManager().readData(world, nbt.get("players"));
        }
        readData(nbt);
    }

    public static long createGameID() {
        Random random = new Random();
        long id;
        do {
            id = random.nextLong();
        } while (id <= 0);
        return id;
    }

    public GameStorage getStorage() {
        return WorldDataFactory.getData(world).getStorage();
    }
}
