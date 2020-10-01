package dev.toma.pubgmc.capability.world;

import dev.toma.pubgmc.RegistryHandler;
import dev.toma.pubgmc.capability.IWorldCap;
import dev.toma.pubgmc.games.Game;
import dev.toma.pubgmc.games.GameType;
import dev.toma.pubgmc.games.util.GameStorage;
import dev.toma.pubgmc.network.NetworkManager;
import dev.toma.pubgmc.network.packet.CPacketSyncWorldData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class WorldDataFactory implements IWorldCap {

    private final World world;
    private Game game;
    private GameStorage storage = new GameStorage();

    public WorldDataFactory(World world) {
        this.world = world;
    }

    public WorldDataFactory() {
        this(null);
    }

    public static IWorldCap getData(World world) {
        return world.getCapability(WorldDataProvider.CAP, null).orElse(null);
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public GameStorage getStorage() {
        return storage;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("storage", storage.serializeNBT());
        if(game != null) {
            CompoundNBT gameNBT = new CompoundNBT();
            gameNBT.putString("key", game.getType().getRegistryName().toString());
            gameNBT.put("data", game.serializeNBT());
            nbt.put("game", gameNBT);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        storage.deserializeNBT(nbt.contains("storage", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("storage") : new CompoundNBT());
        if(nbt.contains("game", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT gameNBT = nbt.getCompound("game");
            ResourceLocation key = new ResourceLocation(gameNBT.getString("key"));
            GameType<?> type = RegistryHandler.GAMES.getValue(key);
            if(type != null) {
                Game game = type.newGame(world);
                game.deserializeNBT(gameNBT.getCompound("data"));
                this.game = game;
            }
        }
    }

    @Override
    public void sync() {
        if(!world.isRemote) {
            CompoundNBT nbt = serializeNBT();
            NetworkManager.sendToAll(world, new CPacketSyncWorldData(nbt));
        }
    }

    public static class Storage implements Capability.IStorage<IWorldCap> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IWorldCap> capability, IWorldCap instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IWorldCap> capability, IWorldCap instance, Direction side, INBT nbt) {
            instance.deserializeNBT(nbt instanceof CompoundNBT ? (CompoundNBT) nbt : new CompoundNBT());
        }
    }
}
