package dev.toma.pubgmc.common.entity.vehicle.land;

import dev.toma.pubgmc.common.entity.vehicle.DriveableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
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
}
