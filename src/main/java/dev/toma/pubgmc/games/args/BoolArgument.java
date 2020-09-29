package dev.toma.pubgmc.games.args;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.Constants;

public class BoolArgument extends GameArgument<Boolean> {

    public BoolArgument(boolean value) {
        super(value, Constants.NBT.TAG_BYTE);
    }

    @Override
    public Boolean load(INBT nbt) {
        return ((ByteNBT) nbt).getByte() != 0;
    }
}
