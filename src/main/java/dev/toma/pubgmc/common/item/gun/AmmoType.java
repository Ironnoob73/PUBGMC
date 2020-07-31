package dev.toma.pubgmc.common.item.gun;

import dev.toma.pubgmc.Registry;
import dev.toma.pubgmc.common.item.utility.AmmoItem;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public enum AmmoType {

    CROSSBOW_BOLT(() -> Registry.PMCItems.CROSSBOW_BOLT, 5),
    AMMO_12G(() -> Registry.PMCItems.AMMO_12G, 10),
    AMMO_9MM(() -> Registry.PMCItems.AMMO_9MM, 30),
    AMMO_45ACP(() -> Registry.PMCItems.AMMO_45ACP, 30),
    AMMO_556MM(() -> Registry.PMCItems.AMMO_556MM, 30),
    AMMO_762MM(() -> Registry.PMCItems.AMMO_762MM, 30),
    AMMO_300M(() -> Registry.PMCItems.AMMO_300M, 10);

    private final Supplier<AmmoItem> item;
    private final int genCount;

    AmmoType(Supplier<AmmoItem> supplier, int count) {
        this.item = supplier;
        this.genCount = count;
    }

    public AmmoItem getAmmo() {
        return item.get();
    }

    public ItemStack generateAmmoDrop() {
        return new ItemStack(this.getAmmo(), genCount);
    }
}
