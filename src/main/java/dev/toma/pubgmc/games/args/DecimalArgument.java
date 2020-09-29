package dev.toma.pubgmc.games.args;

import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.Constants;

public class DecimalArgument extends GameArgument<Double> {

    public DecimalArgument(double value) {
        super(value, Constants.NBT.TAG_DOUBLE);
    }

    @Override
    public Double load(INBT nbt) {
        return ((DoubleNBT) nbt).getDouble();
    }
}
