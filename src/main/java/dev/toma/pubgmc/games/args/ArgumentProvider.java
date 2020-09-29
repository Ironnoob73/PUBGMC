package dev.toma.pubgmc.games.args;

import dev.toma.pubgmc.games.GameType;

import java.util.function.Function;

public class ArgumentProvider {

    public static final String DURATION = "duration";
    public static final Function<GameType<?>, Integer> DURATION_ARGUMENT = type -> type.getGameArguments().getInt(DURATION);
}
