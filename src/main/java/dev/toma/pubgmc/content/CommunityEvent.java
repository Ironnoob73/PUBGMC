package dev.toma.pubgmc.content;

import com.google.gson.*;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Objects;

public class CommunityEvent {

    private final String name;
    private final LocalDateTime dateTime;
    private final String host;
    @Nullable private final String[] description;

    private CommunityEvent(@Nullable String name, LocalDateTime dateTime, @Nullable String host, @Nullable String[] description) {
        this.name = name != null && !name.isEmpty() ? name : "Unnamed event";
        this.dateTime = Objects.requireNonNull(dateTime, "Undefined event schedule");
        this.host = host != null && !host.isEmpty() ? host : "Unkown";
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getScheduledDateTime() {
        return dateTime;
    }

    public String getHost() {
        return host;
    }

    @Nullable
    public String[] getDescription() {
        return description;
    }

    public boolean hasDescription() {
        return description != null && description.length > 0;
    }

    public static class Deserializer implements JsonDeserializer<CommunityEvent> {

        @Override
        public CommunityEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            String name = object.get("name").getAsString();
            String host = object.get("host").getAsString();
            JsonArray description = object.getAsJsonArray("description");
            int pos = 0;
            String[] strings = new String[description.size()];
            for (JsonElement element : description) {
                strings[pos] = element.getAsString();
                ++pos;
            }
            LocalDateTime dateTime = LocalDateTime.parse(object.get("date").getAsString());
            return new CommunityEvent(name, dateTime, host, strings);
        }
    }
}
