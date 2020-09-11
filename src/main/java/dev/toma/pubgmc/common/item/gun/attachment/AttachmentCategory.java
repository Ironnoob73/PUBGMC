package dev.toma.pubgmc.common.item.gun.attachment;

public enum AttachmentCategory {

    BARREL(18, 33),
    GRIP(52, 88),
    MAGAZINE(101, 88),
    STOCK(142, 33),
    SCOPE(79, 14);

    final int x, y;

    AttachmentCategory(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getSlotTexture() {
        return "pubgmc:slot/" + name().toLowerCase();
    }
}
