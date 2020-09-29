package dev.toma.pubgmc.capability;

import dev.toma.pubgmc.games.Game;
import dev.toma.pubgmc.games.util.GameStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public interface IWorldCap extends INBTSerializable<CompoundNBT> {

    World getWorld();

    Game getGame();

    void setGame(Game game);

    GameStorage getStorage();
}
