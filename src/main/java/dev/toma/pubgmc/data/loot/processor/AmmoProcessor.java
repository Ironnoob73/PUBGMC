package dev.toma.pubgmc.data.loot.processor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.pubgmc.common.items.guns.AmmoType;
import dev.toma.pubgmc.common.items.guns.GunBase;
import dev.toma.pubgmc.data.loot.LootGenerationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.World;

import java.util.List;

public class AmmoProcessor implements LootProcessor {

    private final int packSize;
    private final int packCountMin;
    private final int packCountMax;

    public AmmoProcessor(int packSize, int packCountMin, int packCountMax) {
        this.packSize = packSize;
        this.packCountMin = packCountMin;
        this.packCountMax = packCountMax;
    }

    @Override
    public void processItems(LootGenerationContext context, List<ItemStack> generated) {
        if (generated.isEmpty()) {
            return;
        }
        ItemStack stack = generated.get(0);
        if (!(stack.getItem() instanceof GunBase)) {
            return;
        }
        World world = context.getWorld();
        GunBase gun = (GunBase) stack.getItem();
        AmmoType ammoType = gun.getAmmoType();
        int count = packCountMin + world.rand.nextInt(packCountMax - packCountMin + 1);
        for (int i = 0; i < count; i++) {
            ItemStack itemStack = new ItemStack(ammoType.ammo(), packSize);
            generated.add(itemStack);
        }
    }

    @Override
    public LootProcessorType<?> getType() {
        return LootProcessors.AMMO_PROCESSOR;
    }

    public static final class Serializer implements LootProcessorSerializer<AmmoProcessor> {

        private static final int DEFAULT_PACK_SIZE = 30;
        private static final int DEFAULT_PACK_MIN = 1;
        private static final int DEFAULT_PACK_MAX = 3;

        @Override
        public AmmoProcessor parse(JsonObject object) throws JsonParseException {
            int size = JsonUtils.getInt(object, "count", DEFAULT_PACK_SIZE);
            int min = JsonUtils.getInt(object, "minPacks", DEFAULT_PACK_MIN);
            int max = JsonUtils.getInt(object, "maxPacks", DEFAULT_PACK_MAX);
            if (size < 1) {
                throw new JsonSyntaxException("Pack size cannot be lower than 1");
            }
            if (min > max) {
                throw new JsonSyntaxException("Min pack count cannot be bigger than max pack count");
            }
            if (max < 0) {
                throw new JsonSyntaxException("Max pack count cannot be lower than 1");
            }
            return new AmmoProcessor(size, min, max);
        }

        @Override
        public void serialize(JsonObject object, AmmoProcessor processor) {
            if (processor.packSize != DEFAULT_PACK_SIZE) {
                object.addProperty("count", processor.packSize);
            }
            if (processor.packCountMin != DEFAULT_PACK_MIN) {
                object.addProperty("minPacks", processor.packCountMin);
            }
            if (processor.packCountMax != DEFAULT_PACK_MAX) {
                object.addProperty("maxPacks", processor.packCountMax);
            }
        }
    }
}
