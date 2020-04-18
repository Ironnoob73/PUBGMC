package dev.toma.pubgmc.common.entity.vehicle;

import dev.toma.pubgmc.Registry;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AirDriveableEntity extends DriveableEntity {

    public float throttle = 0.0F;

    public AirDriveableEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public AirDriveableEntity(EntityType<?> type, World world, BlockPos pos, DriveableData data) {
        super(type, world, pos, data);
    }

    @Override
    protected void updateEntityPost() {
        currentSpeed = UsefulFunctions.wrap(throttle > 0F ? currentSpeed + data.getAcceleration() * throttle : currentSpeed - data.getBrakeSpeed(), 0.0F, this.maxSpeed());
        this.setMotion(this.getMotion().x, currentSpeed > 2.0F ? this.getMotion().y : -0.25, this.getMotion().z);
        move(MoverType.SELF, this.getMotion());
        if(!this.isBeingRidden()) {
            resetInputState();
        }
    }

    // pitchDown
    @Override
    protected void moveForward() {
        if(!onGround) rotationPitch = UsefulFunctions.wrap(rotationPitch + 1.0F, -30.0F, 30.0F);
    }

    // pitchUp
    @Override
    protected void moveBackward() {
        if(currentSpeed > 1.0F) rotationPitch = UsefulFunctions.wrap(rotationPitch - 1.0F, -30.0F, 30.0F);
    }

    @Override
    protected void onPitchUpKey() {
        if(hasFuel() && submergedHeight < 1.0D) throttle = Math.min(1.0F, throttle + 0.04F);
    }

    @Override
    protected void onPitchDownKey() {
        throttle = Math.max(0.0F, throttle - 0.04F);
    }

    private float maxSpeed() {
        return data.getMaxSpeed() * throttle;
    }

    public static class GliderDriveable extends AirDriveableEntity {
        public static final DriveableData DATA = new DriveableData(50F, 100.0F, 4.0F, 0.025F, 0.2F);
        public GliderDriveable(EntityType<?> type, World world) {
            super(type, world);
        }
        public GliderDriveable(World world, BlockPos pos) {
            super(Registry.PMCEntityTypes.GLIDER, world, pos, DATA);
        }
    }
}
