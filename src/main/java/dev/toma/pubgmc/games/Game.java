package dev.toma.pubgmc.games;

import java.util.Random;

public interface Game {

    long getGameID();

    default long createGameID() {
        Random random = new Random();
        long id;
        do {
            id = random.nextLong();
        } while (id <= 0);
        return id;
    }
}
