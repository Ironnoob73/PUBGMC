package dev.toma.pubgmc.common.item.gun;

public enum Firemode {

    SINGLE,
    BURST,
    FULL_AUTO;

    public static Firemode singleMode(Firemode firemode) {
        return firemode;
    }

    public static Firemode singleToBurst(Firemode firemode) {
        return firemode == SINGLE ? BURST : SINGLE;
    }

    public static Firemode singleToAuto(Firemode firemode) {
        return firemode == SINGLE ? FULL_AUTO : SINGLE;
    }

    public static Firemode allModes(Firemode firemode) {
        int i = firemode.ordinal();
        if(i + 1 > 2) return SINGLE;
        else return from(i + 1);
    }

    public static Firemode from(int id) {
        return values()[Math.min(2, Math.max(0, id))];
    }
}
