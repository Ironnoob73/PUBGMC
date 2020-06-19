package dev.toma.pubgmc.util.object;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Optional<T> {

    private T value;

    public static <T> Optional<T> of(T value) {
        return new Optional<>(value);
    }

    public static <T> Optional<T> empty() {
        return new Optional<>(null);
    }

    private Optional(T value) {
        this.value = value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public void ifPresent(Consumer<T> action) {
        if(value != null) action.accept(value);
    }

    public T get() {
        return value;
    }

    public T or(T value) {
        if(this.value == null) return value;
        else return this.value;
    }

    public void map(T t) {
        this.value = t;
    }

    public T orMap(T t) {
        if(value == null) map(t);
        return value;
    }

    public <EXCEPTION extends Exception> T orThrow(Supplier<EXCEPTION> exceptionSupplier) throws EXCEPTION {
        if(value != null) return value;
        else throw exceptionSupplier.get();
    }

    public void clear() {
        this.value = null;
    }
}
