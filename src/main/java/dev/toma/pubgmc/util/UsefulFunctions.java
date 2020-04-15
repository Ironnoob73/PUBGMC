package dev.toma.pubgmc.util;

import java.util.function.Predicate;

public class UsefulFunctions {

    public static <T> Predicate<T> alwaysTruePredicate() {
        return p -> true;
    }

    public static int wrap(int number, int min, int max) {
        return number < min ? min : number > max ? max : number;
    }

    public static float wrap(float number, float min, float max) {
        return number < min ? min : number > max ? max : number;
    }
}
