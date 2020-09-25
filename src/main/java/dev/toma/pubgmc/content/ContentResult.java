package dev.toma.pubgmc.content;

import com.google.gson.*;
import dev.toma.pubgmc.client.screen.menu.RefreshListener;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentResult {

    private MenuDisplayContent[] menuDisplayContents;
    private final MapData[] mapData;
    private String[] news;
    private boolean changed;

    public ContentResult(MenuDisplayContent[] menuDisplayContents, String[] news, MapData[] mapData) {
        this.menuDisplayContents = menuDisplayContents;
        this.news = news;
        this.mapData = mapData;
    }

    public void updateModifiable(ContentResult other) {
        this.changed = this.menuDisplayContents.length != other.menuDisplayContents.length || !Arrays.equals(news, other.news);
        this.menuDisplayContents = other.menuDisplayContents;
        this.news = other.news;
        Minecraft mc = Minecraft.getInstance();
        if(changed && mc.currentScreen instanceof RefreshListener) {
            synchronized (Minecraft.getInstance()) {
                ((RefreshListener) mc.currentScreen).onRefresh();
            }
        }
    }

    public boolean hasChanged() {
        return changed;
    }

    public MapData[] getMapData() {
        return mapData;
    }

    public MenuDisplayContent[] getMenuDisplayContents() {
        return menuDisplayContents;
    }

    public String[] getNews() {
        return news;
    }

    public static class Deserializer implements JsonDeserializer<ContentResult> {

        @Override
        public ContentResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(!json.isJsonObject()) throw new JsonParseException("Received invalid data - not a Json Object");
            JsonObject object = json.getAsJsonObject();
            JsonArray displayArray = JSONUtils.getJsonArray(object, "display");
            JsonArray newsArray = JSONUtils.getJsonArray(object, "mainMenuText");
            JsonArray mapDataArray = JSONUtils.getJsonArray(object, "map");
            List<MenuDisplayContent> menuDisplayContentList = new ArrayList<>();
            for (JsonElement element : displayArray) {
                menuDisplayContentList.add(context.deserialize(element, MenuDisplayContent.class));
            }
            List<String> stringList = new ArrayList<>();
            for (JsonElement element : newsArray) {
                stringList.add(element.getAsString());
            }
            List<MapData> mapDataList = new ArrayList<>();
            for (JsonElement element : mapDataArray) {
                mapDataList.add(context.deserialize(element, MapData.class));
            }
            return new ContentResult(menuDisplayContentList.toArray(new MenuDisplayContent[0]), stringList.toArray(new String[0]), mapDataList.toArray(new MapData[0]));
        }
    }
}
