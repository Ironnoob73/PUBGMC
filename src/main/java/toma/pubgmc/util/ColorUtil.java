package toma.pubgmc.util;

public class ColorUtil {

    public static int decodeColor16(String code) {
        try {
            return Integer.decode(code);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int decodeColor24(String code) {
        try {
            long color = Long.decode(code);
            return (int) color;
        } catch (NumberFormatException e) {
            return 0xFF << 24;
        }
    }
}
