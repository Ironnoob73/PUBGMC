package toma.pubgmc.util.time;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Duration implements TickTimeProvider {

    public static final Pattern UNIT_PATTERN = Pattern.compile("\\{[a-zA-Z]*}");
    public static final Pattern COUNT_PATTERN = Pattern.compile("\\{%[a-zA-Z]*}");

    private final int ticks;
    private String cachedFormattedTime;
    private TimeFormatType usedFormatting;

    private Duration(TimeUnit unit, int value) {
        this.ticks = unit.getValueInTicks() * value;
    }

    public static Duration of(TimeUnit unit, int value) {
        return new Duration(unit, value);
    }

    public static Duration ticks(int ticks) {
        return of(TimeUnits.TICK, ticks);
    }

    public static Duration seconds(int seconds) {
        return of(TimeUnits.SECOND, seconds);
    }

    public static Duration minutes(int minutes) {
        return of(TimeUnits.MINUTE, minutes);
    }

    public static Duration hours(int hours) {
        return of(TimeUnits.HOUR, hours);
    }

    public static Duration days(int days) {
        return of(TimeUnits.DAY, days);
    }

    @Override
    public int getTimeValue(TimeUnit unit) {
        return ticks / unit.getValueInTicks();
    }

    @Override
    public String format(String pattern, TimeFormatType type) {
        this.generateCachedTime(pattern, type);
        return cachedFormattedTime;
    }

    public Duration add(TimeUnit unit, int value) {
        int ticks = unit.getValueInTicks() * value;
        return ticks(this.ticks + ticks);
    }

    public Duration subtract(TimeUnit unit, int value) {
        return add(unit, -value);
    }

    private void generateCachedTime(String pattern, TimeFormatType type) {
        if (cachedFormattedTime == null || usedFormatting != type) {
            int pool = this.ticks;
            usedFormatting = type;
            cachedFormattedTime = pattern;
            Matcher unitMatcher = UNIT_PATTERN.matcher(pattern);
            while (unitMatcher.find()) {
                String text = unitMatcher.group();
                TimeUnit timeUnit = this.getUnit(text, 1);
                int value = pool / timeUnit.getValueInTicks();
                String time = String.valueOf(value);
                if (type.shouldPad()) {
                    int expLength = text.length() - 2;
                    time = StringUtils.leftPad(time, expLength, '0');
                }
                pool %= timeUnit.getValueInTicks();
                cachedFormattedTime = cachedFormattedTime.replaceAll(Pattern.quote(text), time);
            }
            pool = ticks;
            Matcher modifierMatcher = COUNT_PATTERN.matcher(pattern);
            while (modifierMatcher.find()) {
                String text = modifierMatcher.group();
                TimeUnit timeUnit = this.getUnit(text, 2);
                int value = pool / timeUnit.getValueInTicks();
                pool %= timeUnit.getValueInTicks();
                boolean multiple = value > 1;
                String replacement = multiple ? "s" : "";
                cachedFormattedTime = cachedFormattedTime.replaceAll(Pattern.quote(text), replacement);
            }
        }
    }

    private TimeUnit getUnit(String text, int startIndex) {
        char unit = text.toCharArray()[startIndex];
        TimeUnit timeUnit = TimeUnits.getByKey(unit);
        if (timeUnit == null) {
            throw new IllegalArgumentException("Unknown unit: " + unit);
        }
        return timeUnit;
    }
}
