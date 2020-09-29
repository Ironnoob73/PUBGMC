package dev.toma.pubgmc.games.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.util.Constants;

public class Area {

    protected BlockPos location;
    protected int radius;

    public static Area defaultInstance() {
        return new Area(null, 0);
    }

    public Area(BlockPos pos, int radius) {
        this.location = pos;
        this.radius = radius;
    }

    public Area(CompoundNBT nbt) {
        radius = nbt.getInt("rad");
        if(radius > 0 && nbt.contains("pos", Constants.NBT.TAG_INT_ARRAY)) {
            int[] arr = nbt.getIntArray("pos");
            if(arr.length != 3) return;
            location = new BlockPos(arr[0], arr[1], arr[2]);
        }
    }

    public boolean contains(Vec3i vec3i) {
        if(location == null)
            return false;
        int dx = Math.abs(location.getX() - vec3i.getX());
        int dy = Math.abs(location.getY() - vec3i.getY());
        int dz = Math.abs(location.getZ() - vec3i.getZ());
        return dx <= radius && dy <= radius && dz <= radius;
    }

    public BlockPos getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        boolean created = radius > 0 && location != null;
        nbt.putInt("rad", radius);
        if(created) {
            nbt.putIntArray("pos", new int[]{location.getX(), location.getY(), location.getZ()});
        }
        return nbt;
    }

    public void invalidate() {
        location = null;
        radius = 0;
    }

    public boolean isValid() {
        return location != null && radius > 0;
    }

    public BlockPos getRandomPosition(World world, boolean onTop) {
        int minX = location.getX() - radius;
        int minZ = location.getZ() - radius;
        int dist = radius * 2;
        int genX = minX + world.rand.nextInt(dist);
        int genZ = minZ + world.rand.nextInt(dist);
        return new BlockPos(genX, onTop ? world.getHeight(Heightmap.Type.WORLD_SURFACE, genX, genZ) : location.getY(), genZ);
    }
}
