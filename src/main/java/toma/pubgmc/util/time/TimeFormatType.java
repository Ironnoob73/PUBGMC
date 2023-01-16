package toma.pubgmc.util.time;

public enum TimeFormatType {

    PADDED,
    SLIM;

    public boolean shouldPad() {
        return this == PADDED;
    }
}
