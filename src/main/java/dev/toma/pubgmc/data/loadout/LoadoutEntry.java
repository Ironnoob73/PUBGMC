package dev.toma.pubgmc.data.loadout;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;

import java.util.Random;

public interface LoadoutEntry {

    void processFor(LivingEntity entity);

    class Basic implements LoadoutEntry {

        private final ItemStack[] weapons;
        private final ItemStack[] chestplate;
        private final ItemStack[] helmet;

        public Basic(ItemStack[] weapons, ItemStack[] chestplate, ItemStack[] helmet) {
            this.weapons = weapons;
            this.chestplate = chestplate;
            this.helmet = helmet;
        }

        @Override
        public void processFor(LivingEntity entity) {
            Random random = entity.world.rand;
            ItemStack weapon = weapons.length == 0 ? ItemStack.EMPTY : UsefulFunctions.random(weapons, random).copy();
            ItemStack chest = random.nextFloat() <= 0.6F && chestplate.length > 0 ? UsefulFunctions.random(chestplate, random).copy() : ItemStack.EMPTY;
            ItemStack head = random.nextFloat() <= 0.5F && helmet.length > 0 ? UsefulFunctions.random(helmet, random).copy() : ItemStack.EMPTY;
            if(!weapon.isEmpty())
                entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, weapon);
            if(!chest.isEmpty())
                entity.setItemStackToSlot(EquipmentSlotType.CHEST, chest);
            if(!head.isEmpty())
                entity.setItemStackToSlot(EquipmentSlotType.HEAD, head);
        }

        public static Basic load(JsonObject object, JsonDeserializationContext context) {
            return new Basic(getStackArray(JSONUtils.getJsonArray(object, "weapon"), context), getStackArray(JSONUtils.getJsonArray(object, "armor"), context), getStackArray(JSONUtils.getJsonArray(object, "helmet"), context));
        }

        static ItemStack[] getStackArray(JsonArray array, JsonDeserializationContext ctx) {
            if(array == null || array.size() == 0) {
                return new ItemStack[0];
            }
            ItemStack[] stacks = new ItemStack[array.size()];
            for (int i = 0; i < stacks.length; i++) {
                stacks[i] = ctx.deserialize(array.get(i), ItemStack.class);
            }
            return stacks;
        }
    }
}
