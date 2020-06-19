package dev.toma.pubgmc.config.enums;

public enum EnumSpeedUnit {

    METRIC(3.6F, "km/h"),
    IMPERIAL(2.2369F, "mph"),
    BLOCKS(1.0F, "b/s");

    private final float multiplier;
    private final String displayName;

    EnumSpeedUnit(float multiplier, String displayName) {
        this.multiplier = multiplier;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public float calculate(float base) {
        return base * this.multiplier;
    }

    public float calculate(double base) {
        return (float) (base * this.multiplier);
    }

    public String asString(double speed) {
        return calculate(speed) + " " + this.displayName;
    }
}
