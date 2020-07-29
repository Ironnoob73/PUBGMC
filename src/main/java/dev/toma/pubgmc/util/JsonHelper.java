package dev.toma.pubgmc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class JsonHelper {

    public static String getString(String name, JsonObject object, Supplier<JsonParseException> exceptionSupplier) throws JsonParseException {
        return get(name, object, (s, o) -> o.getAsJsonPrimitive(s).getAsString(), exceptionSupplier);
    }

    public static int getInt(String name, JsonObject object, Supplier<JsonParseException> exceptionSupplier) throws JsonParseException {
        return get(name, object, (s, o) -> o.getAsJsonPrimitive(s).getAsInt(), exceptionSupplier);
    }

    public static JsonArray getArray(String name, JsonObject object, Supplier<JsonParseException> exceptionSupplier) throws JsonParseException {
        return get(name, object, (s, o) -> o.getAsJsonArray(s), exceptionSupplier);
    }

    public static JsonObject getObject(String name, JsonObject object, Supplier<JsonParseException> exceptionSupplier) throws JsonParseException {
        return get(name, object, (s, o) -> o.getAsJsonObject(s), exceptionSupplier);
    }

    public static <T> T get(String name, JsonObject object, BiFunction<String, JsonObject, T> function, Supplier<JsonParseException> exceptionSupplier) throws JsonParseException {
        if(!object.has(name)) {
            throw exceptionSupplier.get();
        }
        return function.apply(name, object);
    }
}
