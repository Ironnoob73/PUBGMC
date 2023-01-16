package toma.pubgmc.util.time;

import java.util.HashMap;
import java.util.Map;

public final class TimeUnits {

    private static final Map<Character, TimeUnit> CHAR2UNIT_MAP = new HashMap<>();

    // Standart units
    public static final TimeUnit TICK = register(TimeUnitType.create("tick", 't', 1));
    public static final TimeUnit SECOND = register(TimeUnitType.create("second", 's', 20));
    public static final TimeUnit MINUTE = register(TimeUnitType.create("minute", 'm', SECOND.getValueInTicks() * 60));
    public static final TimeUnit HOUR = register(TimeUnitType.create("hour", 'h', MINUTE.getValueInTicks() * 60));
    public static final TimeUnit DAY = register(TimeUnitType.create("day", 'd', HOUR.getValueInTicks() * 24));
    public static final TimeUnit MONTH = register(TimeUnitType.create("month", 'M', DAY.getValueInTicks() * 30));
    public static final TimeUnit YEAR = register(TimeUnitType.create("year", 'Y', DAY.getValueInTicks() * 365));
    // Special units
    public static final TimeUnit MC_DAY_CYCLE = register(TimeUnitType.create("minecraft day", 'D', 24000));

    // Utility methods
    public static TimeUnit register(TimeUnit unit) {
        if (CHAR2UNIT_MAP.put(unit.getTimeMarker(), unit) != null) {
            throw new IllegalStateException("Unit with " + unit.getTimeMarker() + " identifier already exists!");
        }
        return unit;
    }

    public static TimeUnit getByKey(char key) {
        return CHAR2UNIT_MAP.get(key);
    }
}
