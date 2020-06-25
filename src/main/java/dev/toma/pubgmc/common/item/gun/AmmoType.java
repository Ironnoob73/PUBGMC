package dev.toma.pubgmc.common.item.gun;

import dev.toma.pubgmc.Registry;
import dev.toma.pubgmc.common.item.utility.AmmoItem;

import java.util.function.Supplier;

public enum AmmoType {

    CROSSBOW_BOLT(() -> Registry.PMCItems.CROSSBOW_BOLT),
    AMMO_12G(() -> Registry.PMCItems.AMMO_12G),
    AMMO_9MM(() -> Registry.PMCItems.AMMO_9MM),
    AMMO_45ACP(() -> Registry.PMCItems.AMMO_45ACP),
    AMMO_556MM(() -> Registry.PMCItems.AMMO_556MM),
    AMMO_762MM(() -> Registry.PMCItems.AMMO_762MM),
    AMMO_300M(() -> Registry.PMCItems.AMMO_300M);

    private final Supplier<AmmoItem> item;

    AmmoType(Supplier<AmmoItem> supplier) {
        this.item = supplier;
    }

    public AmmoItem getAmmo() {
        return item.get();
    }
}
