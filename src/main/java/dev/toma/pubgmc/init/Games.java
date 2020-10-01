package dev.toma.pubgmc.init;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.games.BattleRoyaleGame;
import dev.toma.pubgmc.games.DeathmatchGame;
import dev.toma.pubgmc.games.GameType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Pubgmc.MODID)
public class Games {
    public static final GameType<DeathmatchGame> DEATHMATCH = null;
    public static final GameType<BattleRoyaleGame> BATTLE_ROYALE = null;
}
