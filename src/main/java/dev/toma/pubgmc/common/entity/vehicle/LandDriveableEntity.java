package dev.toma.pubgmc.common.entity.vehicle;

import dev.toma.pubgmc.init.PMCEntities;
import dev.toma.pubgmc.init.PMCSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class LandDriveableEntity extends DriveableEntity {

    public boolean isBroken;
    public int timeBeforeBreak;

    public LandDriveableEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public LandDriveableEntity(EntityType<?> type, World world, BlockPos pos, DriveableData data) {
        super(type, world, pos, data);
    }

    @Override
    protected void updateEntityPre() {
        if (this.isInWater()) {
            currentSpeed *= 0.85F;
            if (!isBroken) {
                if (++timeBeforeBreak >= 100) {
                    isBroken = true;
                }
            }
        }
        super.updateEntityPre();
    }

    @Override
    public boolean canAccelerate() {
        return this.hasFuel() && !isBroken;
    }

    /**
     * UAZ Vehicle class
     */
    public static class UAZDriveable extends LandDriveableEntity {

        public static final DriveableData UAZ_DATA = new DriveableData(100.0F, 150.0F, 1.8F, 0.005F, 0.05F);
        private static final Vec3d ENGINE = new Vec3d(1.9, 1.2, 0);
        private static final VehicleSoundStorage SOUND_STORAGE = new VehicleSoundStorage(PMCSounds.UAZ_ACCELERATE, PMCSounds.UAZ_BRAKE, PMCSounds.UAZ_CRUISE, PMCSounds.UAZ_IDLE);

        public UAZDriveable(EntityType<?> type, World world) {
            super(type, world);
        }

        public UAZDriveable(World world, BlockPos pos) {
            super(PMCEntities.UAZ, world, pos, UAZ_DATA);
        }

        @Override
        public int maxUserAmount() {
            return 4;
        }

        @Override
        protected double getPassengerX(int index) {
            return index < 2 ? 0.4 : -0.8;
        }

        @Override
        protected double getPassengerY(int index) {
            return 0.3;
        }

        @Override
        protected double getPassengerZ(int index) {
            return index % 2 == 0 ? -0.6 : 0.6;
        }

        @Override
        public Vec3d getEngineVector() {
            return ENGINE;
        }

        @Override
        public VehicleSoundStorage getSoundStorage() {
            return SOUND_STORAGE;
        }
    }
}
