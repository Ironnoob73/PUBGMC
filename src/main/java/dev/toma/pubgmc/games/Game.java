package dev.toma.pubgmc.games;

import com.google.common.collect.Sets;
import dev.toma.pubgmc.capability.IWorldCap;
import dev.toma.pubgmc.capability.world.WorldDataFactory;
import dev.toma.pubgmc.capability.world.WorldDataProvider;
import dev.toma.pubgmc.games.args.ArgumentMap;
import dev.toma.pubgmc.games.interfaces.*;
import dev.toma.pubgmc.games.util.GameStorage;
import net.minecraft.client.MainWindow;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public abstract class Game implements INBTSerializable<CompoundNBT>, IKeyHolder, GameManager {

    public static final Marker marker = MarkerManager.getMarker("Games");
    public final World world;
    private final GameType<?> type;
    private final Set<IStateListener> stateListeners = Sets.newHashSet();
    private long gameID;
    private IZone zone;

    public Game(GameType<?> type, World world) {
        this.type = Objects.requireNonNull(type, "Game type must be nonnull");
        this.world = Objects.requireNonNull(world, "Game's world must be nonnull");
        this.zone = newZoneInstance(WorldDataFactory.getData(world).getStorage());
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
        this.sync();
    }

    public final void exec_GameStop() {
        if(this instanceof IDataHolder<?>) {
            ((IDataHolder<?>) this).resetData();
        }
        this.onStop();
        this.sync();
    }

    public final void exec_GameTick() {
        boolean sync = false;
        this.getZone().tick(this);
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
        world.getCapability(WorldDataProvider.CAP).ifPresent(IWorldCap::sync);
    }

    /* =============================================================================================================================== */

    public abstract void onStart();

    public abstract void onStop();

    public abstract void onTick();

    public abstract boolean isRunning();

    public abstract IZone newZoneInstance(GameStorage storage);

    public GameType<?> getType() {
        return type;
    }

    @Override
    public IZone getZone() {
        return zone;
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
        nbt.put("zone", getZone().serializeNBT());
        writeData(nbt);
        return nbt;
    }

    @Override
    public final void deserializeNBT(CompoundNBT nbt) {
        gameID = nbt.getLong("gameID");
        if(nbt.contains("players")) {
            getPlayerManager().readData(world, nbt.get("players"));
        }
        if(nbt.contains("zone")) {
            zone = newZoneInstance(WorldDataFactory.getData(world).getStorage());
            zone.deserializeNBT(nbt.getCompound("zone"));
        }
        readData(nbt);
    }

    @OnlyIn(Dist.CLIENT)
    public void renderGameOverlay(MainWindow window, float partialTicks) {

    }

    public <T> T getArgumentValue(Function<ArgumentMap, T> getterFunction) {
        return getterFunction.apply(this.getType().getGameArguments());
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
