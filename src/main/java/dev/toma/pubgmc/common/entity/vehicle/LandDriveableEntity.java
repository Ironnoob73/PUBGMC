package dev.toma.pubgmc.common.entity.vehicle;

import dev.toma.pubgmc.Registry;
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
        if(this.isInWater()) {
            currentSpeed *= 0.85F;
            if(!isBroken) {
                if(++timeBeforeBreak >= 100) {
                    isBroken = true;
                }
            }
        }
    }

    @Override
    public boolean canAccelerate() {
        return this.hasFuel() && !isBroken;
    }

    public static class UAZDriveable extends LandDriveableEntity {
        public static final DriveableData UAZ_DATA = new DriveableData(100.0F, 150.0F, 3.6F, 0.02F, 0.2F);
        public UAZDriveable(EntityType<?> type, World world) {
            super(type, world);
        }
        public UAZDriveable(World world, BlockPos pos) {
            super(Registry.PMCEntityTypes.UAZ, world, pos, UAZ_DATA);
        }
        @Override
        public int maxUserAmount() {
            return 4;
        }
    }
}
