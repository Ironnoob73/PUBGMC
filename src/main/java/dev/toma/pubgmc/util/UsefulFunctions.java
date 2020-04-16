package dev.toma.pubgmc.util;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.function.Predicate;

public class UsefulFunctions {

    public static <T> Predicate<T> alwaysTruePredicate() {
        return p -> true;
    }

    public static double getDistance(Vec3i vec1, Vec3i vec2) {
        return getDistance(vec1.getX(), vec1.getY(), vec1.getZ(), vec2.getX(), vec2.getY(), vec2.getZ());
    }

    public static double getDistance(Vec3d v1, Vec3d v2) {
        return getDistance(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
    }

    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(sqr(x2 - x1) + sqr(y2 - y1) + sqr(z2 - z1));
    }

    public static double sqr(double n) {
        return n * n;
    }

    public static int wrap(int number, int min, int max) {
        return number < min ? min : number > max ? max : number;
    }

    public static float wrap(float number, float min, float max) {
        return number < min ? min : number > max ? max : number;
    }
}
