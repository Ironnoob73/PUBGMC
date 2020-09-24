package dev.toma.pubgmc.content;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import dev.toma.pubgmc.Pubgmc;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ContentManager {

    protected static final Marker marker = MarkerManager.getMarker("Content");
    private final Gson gson = new GsonBuilder().create();
    private final URL url;
    private ContentResult cachedResult;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, r -> {
        Thread thread = new Thread(r);
        thread.setName("Content Thread");
        return thread;
    });
    private ScheduledFuture<?> task;
    volatile boolean hasFailed = false;

    public ContentManager() {
        try {
            url = new URL("https://raw.githubusercontent.com/Toma1O6/PUBGMC/PUBGMC-1.14.4/content.json?token=AKEKPIORUMKFK4VJOPHF3DC7NOJRK");
        } catch (MalformedURLException murle) {
            throw new ReportedException(CrashReport.makeCrashReport(murle, "Couldn't create ContentManager object"));
        }
    }

    public synchronized void start() {
        try {
            task = executorService.scheduleAtFixedRate(this::parseContent, 0, 5, TimeUnit.MINUTES);
        } catch (Throwable throwable) {
            throw new ReportedException(CrashReport.makeCrashReport(throwable, "Fatal error occurred when updating content"));
        }
    }

    private void parseContent() {
        try {
            JsonParser parser = new JsonParser();
            ContentResult contentResult = gson.fromJson(parser.parse(this.getContentRaw(url)), ContentResult.class);
            if(cachedResult == null) {
                this.setContent(contentResult);
            } else {
                this.cachedResult.updateModifiable(contentResult);
            }
            if(hasFailed) {
                hasFailed = false;
                if(task != null) {
                    task.cancel(true);
                }
                task = executorService.scheduleAtFixedRate(this::parseContent, 0, 5, TimeUnit.MINUTES);
            }
        } catch (Exception ex) {
            Pubgmc.pubgmcLog.fatal(marker, "Couldn't parse url content");
            if(!hasFailed) {
                hasFailed = true;
                if(task != null) {
                    task.cancel(true);
                }
                task = executorService.scheduleAtFixedRate(this::parseContent, 30, 30, TimeUnit.SECONDS);
                Pubgmc.pubgmcLog.fatal(marker, "Going to attempt to reconnect every 30s until sucessfull");
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

    public synchronized void setContent(ContentResult result) {
        this.cachedResult = result;
    }
}
