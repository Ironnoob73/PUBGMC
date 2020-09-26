package dev.toma.pubgmc.common.entity.vehicle;

import net.minecraft.util.SoundEvent;

import java.util.function.Function;

public enum VehicleState {

    IDLE(VehicleSoundStorage::getIdle),
    ACCELERATING(VehicleSoundStorage::getAccelerate),
    BRAKING(VehicleSoundStorage::getBrake),
    CRUISING(VehicleSoundStorage::getCruise);

    private final Function<VehicleSoundStorage, SoundEvent> stateSoundFunction;

    VehicleState(Function<VehicleSoundStorage, SoundEvent> stateSoundFunction) {
        this.stateSoundFunction = stateSoundFunction;
    }

    public SoundEvent getSound(VehicleSoundStorage soundStorage) {
        return stateSoundFunction.apply(soundStorage);
    }

    public static VehicleState getState(DriveableEntity entity) {
        float current = entity.currentSpeed;
        float last = entity.lastSpeed;
        if(entity.currentSpeed == 0) {
            return IDLE;
        } else if(current > last) {
            return ACCELERATING;
        } else if(current < last) {
            if(entity.noVerticalKey()) {
                return CRUISING;
            } else return BRAKING;
        }
        return IDLE;
    }
}
