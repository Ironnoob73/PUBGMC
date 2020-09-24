package dev.toma.pubgmc.content;

import com.google.gson.*;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ContentResult {

    private CommunityEvent[] events;
    private MenuAnnouncement[] announcements;
    private final MapData[] mapData;
    private final String[] news;
    private boolean changed;

    public ContentResult(CommunityEvent[] events, MenuAnnouncement[] announcements, String[] news, MapData[] mapData) {
        this.events = events;
        this.announcements = announcements;
        this.news = news;
        this.mapData = mapData;
    }

    public void updateModifiable(ContentResult other) {
        this.changed = this.events.length != other.events.length || this.announcements.length != other.events.length;
        this.events = other.events;
        this.announcements = other.announcements;
    }

    public boolean hasChanged() {
        return changed;
    }

    public MapData[] getMapData() {
        return mapData;
    }

    public CommunityEvent[] getEvents() {
        return events;
    }

    public MenuAnnouncement[] getAnnouncements() {
        return announcements;
    }

    public String[] getNews() {
        return news;
    }

    public static class Deserializer implements JsonDeserializer<ContentResult> {

        @Override
        public ContentResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!json.isJsonObject()) throw new JsonParseException("Received invalid data - not a Json Object");
            JsonObject object = json.getAsJsonObject();
            JsonArray eventArray = JSONUtils.getJsonArray(object, "event");
            JsonArray announcementArray = JSONUtils.getJsonArray(object, "announcement");
            JsonArray newsArray = JSONUtils.getJsonArray(object, "news");
            JsonArray mapDataArray = JSONUtils.getJsonArray(object, "map");
            List<CommunityEvent> communityEventList = new ArrayList<>();
            for (JsonElement element : eventArray) {
                communityEventList.add(context.deserialize(element, CommunityEvent.class));
            }
            List<MenuAnnouncement> menuAnnouncementList = new ArrayList<>();
            for (JsonElement element : announcementArray) {
                menuAnnouncementList.add(context.deserialize(element, MenuAnnouncement.class));
            }
            List<String> stringList = new ArrayList<>();
            for (JsonElement element : newsArray) {
                stringList.add(element.getAsString());
            }
            List<MapData> mapDataList = new ArrayList<>();
            for (JsonElement element : mapDataArray) {
                mapDataList.add(context.deserialize(element, MapData.class));
            }
            return new ContentResult(communityEventList.toArray(new CommunityEvent[0]), menuAnnouncementList.toArray(new MenuAnnouncement[0]), stringList.toArray(new String[0]), mapDataList.toArray(new MapData[0]));
        }
    }
}
