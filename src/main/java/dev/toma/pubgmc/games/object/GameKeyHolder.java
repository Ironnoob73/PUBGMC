package dev.toma.pubgmc.games.object;

import java.util.function.Predicate;

public interface GameKeyHolder extends Predicate<Long> {

    /**
     * Used to update held gameID in this object
     * Useful for example for loot generators to not
     * generate loot multiple times in one game
     * @param ID - new ID to be set
     */
    void setGameID(long ID);

    /**
     * Used to obtain currently held game ID for further processing
     * @return currently contained game ID
     */
    long getGameID();

    /**
     * Compares supplied ID to stored one
     * @param ID - ID to be compared
     * @return true if {@link #getGameID()} and supplied ID are the same
     */
    @Override
    default boolean test(Long ID) {
        return getGameID() == ID;
    }
}
