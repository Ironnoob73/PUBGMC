package dev.toma.pubgmc.games.args;

import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.Constants;

public class StringArgument extends GameArgument<String> {

    public StringArgument(String value) {
        super(value, Constants.NBT.TAG_STRING);
    }

    @Override
    public String load(INBT nbt) {
        return nbt.getString();
    }
}
