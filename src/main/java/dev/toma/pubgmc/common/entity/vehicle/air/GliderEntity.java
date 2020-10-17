package dev.toma.pubgmc.common.entity.vehicle.air;

import dev.toma.pubgmc.common.entity.vehicle.VehicleSoundStorage;
import dev.toma.pubgmc.init.PMCEntities;
import dev.toma.pubgmc.init.PMCSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GliderEntity extends AirDriveableEntity {

    public static final DriveableData DATA = new DriveableData(50F, 100.0F, 1.4F, 0.01F, 0.2F);
    private static final Vec3d ENGINE = new Vec3d(-2.6D, 1.25D, 0.0D);
    private static final VehicleSoundStorage SOUND_STORAGE = new VehicleSoundStorage(PMCSounds.GLIDER_ACCELERATE, PMCSounds.GLIDER_BRAKE, PMCSounds.GLIDER_CRUISE, PMCSounds.GLIDER_IDLE);

    public GliderEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public GliderEntity(World world, BlockPos pos) {
        super(PMCEntities.GLIDER, world, pos, DATA);
    }

    @Override
    public int maxUserAmount() {
        return 2;
    }

    @Override
    protected double getPassengerX(int index) {
        return index == 0 ? -0.5 : -1.5;
    }

    @Override
    protected double getPassengerY(int index) {
        return index == 0 ? 0.4 : 0.8;
    }

    @Override
    protected double getPassengerZ(int index) {
        return 0;
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
