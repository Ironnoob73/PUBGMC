package dev.toma.pubgmc.games.args;

import java.util.function.Function;

public class ArgumentProvider {

    // IDENTIFIERS
    public static final String DURATION = "duration";
    public static final String TEAM_SIZE = "teamSize";
    public static final String ZONE_SHRINK_TIMES = "zoneshrinkTimes";
    public static final String AIRDROP_AMOUNT = "airdropCount";
    // ARGUMENT GETTERS
    public static final Function<ArgumentMap, Integer> DURATION_ARGUMENT = map -> map.getInt(DURATION);
    public static final Function<ArgumentMap, Integer> TEAM_SIZE_ARGUMENT = map -> map.getInt(TEAM_SIZE);
    public static final Function<ArgumentMap, int[]> ZONE_SHRINK_TIMES_ARGUMENT = map -> map.getIntArray(ZONE_SHRINK_TIMES);
    public static final Function<ArgumentMap, Integer> AIRDROP_AMOUNT_ARGUMENT = map -> map.getInt(AIRDROP_AMOUNT);
}