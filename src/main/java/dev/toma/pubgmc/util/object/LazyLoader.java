package dev.toma.pubgmc.util.object;

import java.util.function.Supplier;

public class LazyLoader<T> {

    private Supplier<T> loader;
    private T t;

    public LazyLoader(Supplier<T> loader) {
        this.loader = loader;
    }

    public T get() {
        if(t == null) {
            t = loader.get();
            loader = null;
        }
        return t;
    }
}
