package toma.pubgmc.util.time;

public record TimeUnitType(String fullUnitName, char marker, int ticks) implements TimeUnit {

    public static TimeUnit create(String fullUnitName, char marker, int ticks) {
        return new TimeUnitType(fullUnitName, marker, ticks);
    }

    @Override
    public int getValueInTicks() {
        return ticks;
    }

    @Override
    public char getTimeMarker() {
        return marker;
    }

    @Override
    public String toString() {
        return fullUnitName;
    }
}
