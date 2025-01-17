package dev.toma.pubgmc.common.games.game.battleroyale;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.pubgmc.api.game.GameWorldConfiguration;
import dev.toma.pubgmc.api.game.playzone.Playzone;
import dev.toma.pubgmc.api.game.team.TeamGameConfiguration;
import dev.toma.pubgmc.api.util.Position2;
import dev.toma.pubgmc.common.games.playzone.AbstractDamagingPlayzone;
import dev.toma.pubgmc.common.games.playzone.DynamicPlayzone;
import dev.toma.pubgmc.util.helper.SerializationHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class BattleRoyaleGameConfiguration implements TeamGameConfiguration {

    public final GameWorldConfiguration worldConfiguration = new GameWorldConfiguration();
    public boolean automaticGameJoining = true;
    public int teamSize = 4;
    public float planeSpeed = 1.0F;
    public int planeFlightHeight = 256;
    public int playzoneGenerationDelay = 2400;
    public int entityCount = 64;
    public boolean allowAi = true;
    public boolean allowAiCompanions = true;
    public int aiSpawnInterval = 300;
    public int initialAiSpawnDelay = 1200;
    public ZonePhaseConfiguration[] zonePhases = {
            new ZonePhaseConfiguration(0.65F, 1.0F, 60, 2400, 6000),
            new ZonePhaseConfiguration(0.75F, 1.0F, 40, 1800, 3600),
            new ZonePhaseConfiguration(0.75F, 1.0F, 20, 1200, 2400),
            new ZonePhaseConfiguration(0.50F, 2.0F, 20, 800, 1800),
            new ZonePhaseConfiguration(0.25F, 3.5F, 20, 450, 1200),
            new ZonePhaseConfiguration(0.00F, 5.0F, 20, 1200, 3600)
    };

    @Override
    public void performCorrections() {
        teamSize = Math.max(1, teamSize);
        planeSpeed = MathHelper.clamp(planeSpeed, 0.1F, 10.0F);
        planeFlightHeight = MathHelper.clamp(planeFlightHeight, 15, 270);
        playzoneGenerationDelay = Math.max(0, playzoneGenerationDelay);
        entityCount = Math.max(1, entityCount);
        aiSpawnInterval = Math.max(100, aiSpawnInterval);
        initialAiSpawnDelay = Math.max(0, initialAiSpawnDelay);
        worldConfiguration.correct();
    }

    public NBTTagCompound serialize() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("autoJoin", automaticGameJoining);
        nbt.setInteger("teamSize", teamSize);
        nbt.setInteger("playzoneGenerationDelay", playzoneGenerationDelay);
        nbt.setFloat("planeSpeed", planeSpeed);
        nbt.setInteger("planeFlightHeight", planeFlightHeight);
        nbt.setInteger("entityCount", entityCount);
        nbt.setBoolean("allowAi", allowAi);
        nbt.setBoolean("allowAiCompanions", allowAiCompanions);
        nbt.setInteger("aiSpawnInterval", aiSpawnInterval);
        nbt.setInteger("initialAiSpawnDelay", initialAiSpawnDelay);
        NBTTagList zones = new NBTTagList();
        for (ZonePhaseConfiguration configuration : zonePhases) {
            zones.appendTag(configuration.serialize());
        }
        nbt.setTag("zonePhases", zones);
        nbt.setTag("worldCfg", worldConfiguration.serialize());
        return nbt;
    }

    public static BattleRoyaleGameConfiguration deserialize(NBTTagCompound nbt) {
        BattleRoyaleGameConfiguration configuration = new BattleRoyaleGameConfiguration();
        configuration.automaticGameJoining = nbt.getBoolean("autoJoin");
        configuration.teamSize = nbt.getInteger("teamSize");
        configuration.playzoneGenerationDelay = nbt.getInteger("playzoneGenerationDelay");
        configuration.planeSpeed = nbt.getFloat("planeSpeed");
        configuration.planeFlightHeight = nbt.getInteger("planeFlightHeight");
        configuration.entityCount = nbt.getInteger("entityCount");
        configuration.allowAi = nbt.getBoolean("allowAi");
        configuration.allowAiCompanions = nbt.getBoolean("allowAiCompanions");
        configuration.aiSpawnInterval = nbt.getInteger("aiSpawnInterval");
        configuration.initialAiSpawnDelay = nbt.getInteger("initialAiSpawnDelay");
        NBTTagList zones = nbt.getTagList("zonePhases", Constants.NBT.TAG_COMPOUND);
        configuration.zonePhases = new ZonePhaseConfiguration[zones.tagCount()];
        for (int i = 0; i < zones.tagCount(); i++) {
            NBTTagCompound zoneTag = zones.getCompoundTagAt(i);
            configuration.zonePhases[i] = ZonePhaseConfiguration.deserialize(zoneTag);
        }
        configuration.worldConfiguration.deserialize(nbt.getCompoundTag("worldCfg"));
        return configuration;
    }

    public JsonObject jsonSerialize() {
        JsonObject object = new JsonObject();
        object.addProperty("autoJoin", automaticGameJoining);
        object.addProperty("teamSize", teamSize);
        object.addProperty("playzoneGenerationDelay", playzoneGenerationDelay);
        object.addProperty("planeSpeed", planeSpeed);
        object.addProperty("planeFlightHeight", planeFlightHeight);
        object.addProperty("entityCount", entityCount);
        object.addProperty("allowAi", allowAi);
        object.addProperty("allowAiCompanions", allowAiCompanions);
        object.addProperty("aiSpawnInterval", aiSpawnInterval);
        object.addProperty("initialAiSpawnDelay", initialAiSpawnDelay);
        JsonArray zones = new JsonArray();
        for (ZonePhaseConfiguration configuration : zonePhases) {
            zones.add(configuration.jsonSerialize());
        }
        object.add("zonePhases", zones);
        object.add("worldConfig", worldConfiguration.jsonSerialize());
        return object;
    }

    public static BattleRoyaleGameConfiguration jsonDeserialize(JsonObject object) {
        BattleRoyaleGameConfiguration configuration = new BattleRoyaleGameConfiguration();
        configuration.automaticGameJoining = JsonUtils.getBoolean(object, "autoJoin", true);
        configuration.teamSize = JsonUtils.getInt(object, "teamSize", 1);
        configuration.playzoneGenerationDelay = JsonUtils.getInt(object, "playzoneGenerationDelay", 2400);
        configuration.planeSpeed = JsonUtils.getFloat(object, "planeSpeed", 1.0F);
        configuration.planeFlightHeight = JsonUtils.getInt(object, "planeFlightHeight", 255);
        configuration.entityCount = JsonUtils.getInt(object, "entityCount", 64);
        configuration.allowAi = JsonUtils.getBoolean(object, "allowAi", true);
        configuration.allowAiCompanions = JsonUtils.getBoolean(object, "allowAiCompanions", true);
        configuration.aiSpawnInterval = JsonUtils.getInt(object, "aiSpawnInterval", 300);
        configuration.initialAiSpawnDelay = JsonUtils.getInt(object, "initialAiSpawnDelay", 1200);
        JsonArray zones = JsonUtils.getJsonArray(object, "zonePhases", new JsonArray());
        configuration.zonePhases = new ZonePhaseConfiguration[zones.size()];
        int i = 0;
        for (JsonElement element : zones) {
            configuration.zonePhases[i++] = ZonePhaseConfiguration.jsonDeserialize(element);
        }
        configuration.worldConfiguration.jsonDeserialize(JsonUtils.getJsonObject(object, "worldConfig"));
        return configuration;
    }

    public static final class ZonePhaseConfiguration {

        private final float shrinkScale;
        private final float damage;
        private final int damageInterval;
        private final int shrinkTime;
        private final int shrinkDelay;

        public ZonePhaseConfiguration(float shrinkScale, float damage, int damageInterval, int shrinkTime, int shrinkDelay) {
            this.shrinkScale = MathHelper.clamp(shrinkScale, 0.0F, 1.0F);
            this.damage = Math.max(damage, 0.0F);
            this.damageInterval = Math.max(-1, damageInterval);
            this.shrinkTime = Math.max(0, shrinkTime);
            this.shrinkDelay = Math.max(0, shrinkDelay);
        }

        public AbstractDamagingPlayzone.DamageOptions getDamageOptions() {
            return new AbstractDamagingPlayzone.DamageOptions(damage, damageInterval);
        }

        public DynamicPlayzone.ResizeTarget createNewShrinkTarget(Playzone playzone, Random random) {
            Position2 min = playzone.getPositionMin(1.0F);
            Position2 max = playzone.getPositionMax(1.0F);
            double length = Math.min(Math.abs(min.getX() - max.getX()), Math.abs(min.getZ() - max.getZ()));
            double newLength = length * shrinkScale;
            double minX = min.getX();
            double minZ = min.getZ();
            double diff = length - newLength;
            double x = minX + random.nextDouble() * diff;
            double z = minZ + random.nextDouble() * diff;
            Position2 newMin = new Position2(x, z);
            Position2 newMax = new Position2(x + newLength, z + newLength);
            AbstractDamagingPlayzone.DamageOptions opt = getDamageOptions();
            return new DynamicPlayzone.ResizeTarget(newMin, newMax, opt, shrinkTime, shrinkDelay);
        }

        public NBTTagCompound serialize() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setFloat("scale", shrinkScale);
            nbt.setFloat("damage", damage);
            nbt.setInteger("interval", damageInterval);
            nbt.setInteger("shrinkTime", shrinkTime);
            nbt.setInteger("shrinkDelay", shrinkDelay);
            return nbt;
        }

        public static ZonePhaseConfiguration deserialize(NBTTagCompound nbt) {
            float scale = nbt.getFloat("scale");
            float damage = nbt.getFloat("damage");
            int interval = nbt.getInteger("interval");
            int shrinkTime = nbt.getInteger("shrinkTime");
            int shrinkDelay = nbt.getInteger("shrinkDelay");
            return new ZonePhaseConfiguration(scale, damage, interval, shrinkTime, shrinkDelay);
        }

        private JsonObject jsonSerialize() {
            JsonObject object = new JsonObject();
            object.addProperty("scale", shrinkScale);
            object.addProperty("damage", damage);
            object.addProperty("interval", damageInterval);
            object.addProperty("shrinkTime", shrinkTime);
            object.addProperty("shrinkDelay", shrinkDelay);
            return object;
        }

        private static ZonePhaseConfiguration jsonDeserialize(JsonElement json) {
            JsonObject object = SerializationHelper.asObject(json);
            float scale = JsonUtils.getFloat(object, "scale");
            float damage = JsonUtils.getFloat(object, "damage");
            int interval = JsonUtils.getInt(object, "interval");
            int shrinkTime = JsonUtils.getInt(object, "shrinkTime");
            int shrinkDelay = JsonUtils.getInt(object, "shrinkDelay");
            return new ZonePhaseConfiguration(scale, damage, interval, shrinkTime, shrinkDelay);
        }
    }
}
