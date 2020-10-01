package dev.toma.pubgmc.games.args;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundNBT;

import java.util.Map;
import java.util.function.Consumer;

public class ArgumentMap {

    private final Map<String, GameArgument<?>> map = Maps.newHashMap();
    private boolean isLocked;

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void updateValues(CompoundNBT data) {
        for(Map.Entry<String, GameArgument<?>> entry : map.entrySet()) {
            entry.getValue().load(data, entry.getKey());
        }
    }

    public void putBoolean(String identifier, boolean data) {
        put(identifier, new BoolArgument(data));
    }

    public void putDouble(String identifier, double data) {
        put(identifier, new DecimalArgument(data));
    }

    public void putInt(String identifier, int data) {
        put(identifier, new IntArgument(data));
    }

    public void putString(String identifier, String data) {
        put(identifier, new StringArgument(data));
    }

    public void putIntArray(String identifier, int... data) {
        put(identifier, new IntArrayArgument(data));
    }

    public void put(String identifier, GameArgument<?> argument) {
        if(isLocked) {
            throw new UnsupportedOperationException("You cannot add arguments when registration is finished!");
        }
        map.put(identifier, argument);
    }

    public <T, A extends GameArgument<T>> void putAndApply(String identifier, A argument, Consumer<A> consumer) {
        put(identifier, argument);
        consumer.accept(argument);
    }

    public boolean getBoolean(String identifier) {
        return get(identifier, false);
    }

    public double getDouble(String identifier) {
        return get(identifier, 0.0D);
    }

    public int getInt(String identifier) {
        return get(identifier, 0);
    }

    public String getString(String identifier) {
        return get(identifier, "");
    }

    public int[] getIntArray(String identifier) {
        return get(identifier, new int[0]);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String identifier, T fallback) {
        try {
            if(!map.containsKey(identifier)) {
                throw new IllegalArgumentException("There's no argument for " + identifier + " key!");
            }
            return (T) map.get(identifier).getValue();
        } catch (ClassCastException ex) {
            return fallback;
        }
    }

    public void lock() {
        this.isLocked = true;
    }
}
