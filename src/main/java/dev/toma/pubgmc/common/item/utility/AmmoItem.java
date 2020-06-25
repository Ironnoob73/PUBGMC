package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.common.item.PMCItem;
import dev.toma.pubgmc.common.item.gun.AmmoType;

public class AmmoItem extends PMCItem {

    private final AmmoType type;

    public AmmoItem(String name, AmmoType type) {
        super(name, new Properties().group(UTILITY));
        this.type = type;
    }

    public AmmoType ammoType() {
        return type;
    }
}
