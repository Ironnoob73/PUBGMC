package dev.toma.pubgmc.common.inventory;

public enum SlotType {

    NIGHT_VISION("pubgmc:slot/night_vision"),
    GHILLIE("pubgmc:slot/ghillie"),
    BACKPACK("pubgmc:slot/backpack");

    private final String slotTexture;

    SlotType(String slotTexture) {
        this.slotTexture = slotTexture;
    }

    public String getSlotTexture() {
        return slotTexture;
    }
}
