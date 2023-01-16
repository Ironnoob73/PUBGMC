package toma.pubgmc.util.time;

public interface TimeProvider {

    int getTimeValue(TimeUnit unit);

    String format(String pattern, TimeFormatType formatType);

    default String format(String patter) {
        return format(patter, TimeFormatType.SLIM);
    }
}
