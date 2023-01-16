package toma.pubgmc.util.time;

import java.util.function.Supplier;

public interface TickTimeProvider extends TimeProvider, Supplier<Integer> {

    @Override
    default Integer get() {
        return this.getTimeValue(TimeUnits.TICK);
    }
}
