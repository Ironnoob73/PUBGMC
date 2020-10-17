package dev.toma.pubgmc.content;

import com.google.common.collect.Maps;
import com.google.gson.*;
import dev.toma.pubgmc.client.screen.component.Component;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.reflect.Type;
import java.util.Map;

public abstract class MenuDisplayContent {

    private static final Map<String, EventTypeDeserializer<?>> DESERIALIZERS = Maps.newHashMap();

    public static void registerDeserializers() {
        addDeserializer("event", CommunityEvent::deserialize);
    }

    public void onClicked() {

    }

    @OnlyIn(Dist.CLIENT)
    public Component createComponent(int x, int y, int width, int height) {
        return new Component(x, y, width, height);
    }

    public static class Deserializer implements JsonDeserializer<MenuDisplayContent> {

        @Override
        public MenuDisplayContent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!json.isJsonObject()) throw new JsonParseException("Couldn't parse display content, expected JsonObject");
            JsonObject object = json.getAsJsonObject();
            String type = JSONUtils.getString(object, "type");
            EventTypeDeserializer<?> deserializer = DESERIALIZERS.get(type);
            if(deserializer == null) throw new JsonParseException("Unknown event type: " + type);
            return deserializer.deserialize(object, context);
        }
    }

    static <C extends MenuDisplayContent> void addDeserializer(String key, EventTypeDeserializer<C> deserializer) {
        EventTypeDeserializer<?> value = DESERIALIZERS.put(key, deserializer);
        if(value != null) {
            throw new IllegalStateException("Duplicate deserializer key: " + key);
        }
    }
}
