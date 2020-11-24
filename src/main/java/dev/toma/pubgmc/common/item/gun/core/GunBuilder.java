package dev.toma.pubgmc.common.item.gun.core;

import dev.toma.pubgmc.util.function.Bool2ObjFunction;
import net.minecraft.util.SoundEvent;

public class GunBuilder extends AbstractGunBuilder<GunItem> {

    Bool2ObjFunction<SoundEvent> reloadSound;

    public GunBuilder reloadSound(Bool2ObjFunction<SoundEvent> reloadSound) {
        this.reloadSound = reloadSound;
        return this;
    }

    @Override
    protected GunItem createGunObject(String name) {
        nonnullOrThrow(reloadSound, "Undefined reload sound", name);
        return new GunItem(name, properties.setTEISR(ister), this);
    }
}
