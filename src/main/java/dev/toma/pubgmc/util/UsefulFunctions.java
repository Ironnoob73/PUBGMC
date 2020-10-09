package dev.toma.pubgmc.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class UsefulFunctions {

    public static int totalItemCountInInventory(Item item, IInventory inventory) {
        int c = 0;
        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if(stack.getItem() == item) {
                c += stack.getCount();
            }
        }
        return c;
    }

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

    public static int getElementCount(Map<?, ? extends Collection<?>> map) {
        int c = 0;
        for(Map.Entry<?, ? extends Collection<?>> entry : map.entrySet()) {
            c += entry.getValue().size();
        }
        return c;
    }

    public static <K, V> V getNonnullFromMap(Map<K, V> map, K key, V defaultValue) {
        V v = map.get(key);
        return v != null ? v : defaultValue;
    }

    public static <T> boolean contains(T[] array, T element) {
        return contains(array, element, (a, b) -> a == b);
    }

    public static <T> boolean contains(T[] array, T element, BiPredicate<T, T> comparing) {
        for(T t : array) {
            if(comparing.test(element, t)) {
                return true;
            }
        }
        return false;
    }

    public static String addArgumentDesc(String key, String info, String format) {
        return String.format("%s- %s%s:%s%s --> %s", TextFormatting.GREEN, key, TextFormatting.RED, format, TextFormatting.AQUA, info);
    }

    public static <T> T random(T[] ts, Random random) {
        return ts[random.nextInt(ts.length)];
    }
}
