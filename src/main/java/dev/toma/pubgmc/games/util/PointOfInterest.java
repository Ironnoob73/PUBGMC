package dev.toma.pubgmc.games.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class PointOfInterest {

    private final BlockPos location;
    private final String name;

    public PointOfInterest(BlockPos location, String name) {
        this.location = location;
        this.name = name;
    }

    public PointOfInterest(CompoundNBT nbt) throws RuntimeException {
        int[] arr = nbt.getIntArray("pos");
        this.location = new BlockPos(arr[0], arr[1], arr[2]);
        this.name = nbt.getString("name");
    }

    public BlockPos getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putIntArray("pos", new int[] {location.getX(), location.getY(), location.getZ()});
        nbt.putString("name", name);
        return nbt;
    }
}
