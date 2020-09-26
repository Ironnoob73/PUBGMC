package dev.toma.pubgmc.common.entity.vehicle;

import net.minecraft.util.SoundEvent;

import java.util.Objects;

public class VehicleSoundStorage {

    private final SoundEvent acc, brake, cruise, idle;

    public VehicleSoundStorage(SoundEvent acc, SoundEvent brake, SoundEvent cruise, SoundEvent idle) {
        this.acc = Objects.requireNonNull(acc);
        this.brake = Objects.requireNonNull(brake);
        this.cruise = Objects.requireNonNull(cruise);
        this.idle = Objects.requireNonNull(idle);
    }

    public SoundEvent getAccelerate() {
        return acc;
    }

    public SoundEvent getBrake() {
        return brake;
    }

    public SoundEvent getCruise() {
        return cruise;
    }

    public SoundEvent getIdle() {
        return idle;
    }
}
