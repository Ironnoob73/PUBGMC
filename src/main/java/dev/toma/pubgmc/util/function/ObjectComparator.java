package dev.toma.pubgmc.util.function;

public interface ObjectComparator<A, B> {

    boolean accept(A a, B b);
}
