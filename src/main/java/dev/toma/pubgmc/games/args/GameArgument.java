package dev.toma.pubgmc.games.args;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public abstract class GameArgument<T> {

    private final T fallback;
    private final int type;
    private T value;

    public GameArgument(T fallback, int type) {
        this.fallback = fallback;
        this.type = type;
        this.value = fallback;
    }

    public abstract T load(INBT nbt);

    public void load(CompoundNBT nbt, String key) {
        value = fallback;
        if(nbt.contains(key, type)) {
            value = load(nbt.get(key));
        }
    }

    public T getValue() {
        return value == null ? value = fallback : value;
    }

    public T getFallback() {
        return fallback;
    }
}
