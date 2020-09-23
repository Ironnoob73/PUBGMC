package dev.toma.pubgmc.content;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.toma.pubgmc.Pubgmc;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;

public class ContentManager {

    private final Gson gson = new GsonBuilder().create();
    private final ThreadGroup contentGroup = new ThreadGroup("Content");
    private final Thread contentThread = new Thread(contentGroup, this::executeContentTask);
    private final Thread updateThread = new Thread(contentGroup, this::checkForUpdates);
    private final URL url;

    public ContentManager() {
        try {
            url = new URL("");
        } catch (MalformedURLException murle) {
            CrashReport.makeCrashReport(murle, "Couldn't create ContentManager object");
        }
    }

    public synchronized void start() {
        try {
            updateThread.start();
            contentThread.start();
        } catch (Throwable throwable) {
            CrashReport.makeCrashReport(throwable, "Fatal error occurred when updating content");
        }
    }

    private void executeContentTask() {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(this.getContentRaw());
    }

    private void checkForUpdates() {
        while (true) {
            try {
                // wait some time before updating - 5 minutes
                Thread.sleep(300000L);
            } catch (Exception ex) {
                Pubgmc.pubgmcLog.fatal("");
            }
        }
    }

    private String getContentRaw(URL url) throws Exception {
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String input;
        while ((input = reader.readLine()) != null) {
            builder.append(input);
        }
        reader.close();
        return builder.toString();
    }
}
