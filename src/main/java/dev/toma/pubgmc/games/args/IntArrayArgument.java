package dev.toma.pubgmc.games.args;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraftforge.common.util.Constants;

public class IntArrayArgument extends GameArgument<int[]> {

    private final int length;

    public IntArrayArgument(int[] data) {
        super(data, Constants.NBT.TAG_INT_ARRAY);
        this.length = data.length;
    }

    @Override
    public int[] load(INBT nbt) {
        int[] array = ((IntArrayNBT) nbt).getIntArray();
        int missing = this.length - array.length;
        if(missing == 0) {
            return array;
        } else if(missing > 0) {
            int[] arr = new int[this.length];
            System.arraycopy(array, 0, arr, 0, array.length);
            int[] defaultData = getFallback();
            if (defaultData.length - array.length >= 0) {
                System.arraycopy(defaultData, array.length, arr, array.length, defaultData.length - array.length);
            }
            return arr;
        } else {
            int[] ints = new int[this.length];
            if (ints.length >= 0) {
                System.arraycopy(array, 0, ints, 0, ints.length);
            }
            return ints;
        }
    }
}
