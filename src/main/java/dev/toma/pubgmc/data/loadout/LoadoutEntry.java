package dev.toma.pubgmc.data.loadout;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import dev.toma.pubgmc.common.inventory.IHasInventory;
import dev.toma.pubgmc.util.UsefulFunctions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;

import java.util.Random;

public interface LoadoutEntry {

    void processFor(LivingEntity entity);

    class Basic implements LoadoutEntry {

        private ItemStack[] weapons;
        private ItemStack[] chestplate;
        private ItemStack[] helmet;
        private ItemStack[] backpack;

        public Basic(ItemStack[] weapons) {
            this.weapons = weapons;
        }

        @Override
        public void processFor(LivingEntity entity) {
            Random random = entity.world.rand;
            ItemStack weapon = weapons.length == 0 ? ItemStack.EMPTY : UsefulFunctions.random(weapons, random).copy();
            if(entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                player.addItemStackToInventory(weapon);
            } else if(entity instanceof IHasInventory) {
                entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, weapon);
            }
        }

        public static Basic load(JsonObject object, JsonDeserializationContext context) {
            JsonArray weapons = JSONUtils.getJsonArray(object, "weapon");
            ItemStack[] w = new ItemStack[weapons.size()];
            for (int i = 0; i < w.length; i++) {
                w[i] = context.deserialize(weapons.get(i), ItemStack.class);
            }
            return new Basic(w);
        }
    }
}
