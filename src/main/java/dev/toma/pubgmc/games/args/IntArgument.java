package dev.toma.pubgmc.games.args;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraftforge.common.util.Constants;

public class IntArgument extends GameArgument<Integer> {

    public IntArgument(int value) {
        super(value, Constants.NBT.TAG_INT);
    }

    @Override
    public Integer load(INBT nbt) {
        return ((IntNBT) nbt).getInt();
    }
}
