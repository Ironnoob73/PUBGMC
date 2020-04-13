package dev.toma.pubgmc.util;

import java.util.function.Predicate;

public class UsefulFunctions {

    public static <T> Predicate<T> alwaysTruePredicate() {
        return p -> true;
    }

    public static int wrapBetween(int number, int min, int max) {
        return number < min ? min : number > max ? max : number;
    }
}
