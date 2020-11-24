package dev.toma.pubgmc.common.item.gun.core;

import dev.toma.pubgmc.util.function.Bool2IntFunction;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

public class BoltGunBuilder extends GunBuilder {

    Supplier<SoundEvent> boltSound;
    Bool2IntFunction chamberTime;

    public BoltGunBuilder boltSound(Supplier<SoundEvent> event) {
        this.boltSound = event;
        return this;
    }

    public BoltGunBuilder chamberTime(Bool2IntFunction chamberTime) {
        this.chamberTime = chamberTime;
        return this;
    }

    @Override
    protected GunItem createGunObject(String name) {
        return new BoltGunItem(name, properties.setTEISR(ister), this);
    }
}
