package dev.toma.pubgmc.common.games;

import dev.toma.pubgmc.api.game.loadout.EntityLoadout;
import dev.toma.pubgmc.api.game.loadout.LoadoutManager;
import dev.toma.pubgmc.api.inventory.SpecialInventoryProvider;
import dev.toma.pubgmc.common.capability.player.IPlayerData;
import dev.toma.pubgmc.common.capability.player.PlayerData;
import dev.toma.pubgmc.common.capability.player.SpecialEquipmentSlot;
import dev.toma.pubgmc.common.entity.bot.EntityAIPlayer;
import dev.toma.pubgmc.common.games.game.battleroyale.BattleRoyaleGame;
import dev.toma.pubgmc.common.items.equipment.SpecialInventoryItem;
import dev.toma.pubgmc.data.loot.*;
import dev.toma.pubgmc.data.loot.processor.AmmoProcessor;
import dev.toma.pubgmc.data.loot.processor.LootProcessor;
import dev.toma.pubgmc.init.PMCItems;
import dev.toma.pubgmc.util.helper.SerializationHelper;
import dev.toma.pubgmc.util.math.WeightedRandom;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class DefaultEntityLoadouts {

    public static void register() {
        // Loadout handlers
        LoadoutManager.registerLoadoutHandler(EntityPlayerMP.class, DefaultEntityLoadouts::applyPlayerLoadout);
        LoadoutManager.registerLoadoutHandler(EntityAIPlayer.class, DefaultEntityLoadouts::applyAiLoadout);

        // Loadouts
        registerBattleRoyaleLoadouts();
    }

    private static void registerBattleRoyaleLoadouts() {
        // Declarations
        LootProcessor pistolAmmoPack = new AmmoProcessor(15, 1, 3);
        LootProcessor shotgunAmmoPack = new AmmoProcessor(5, 1, 3);
        LootProcessor defaultAmmoPack = new AmmoProcessor(30, 1, 3);
        Function<List<LootProcessor>, LootProvider> pistolsProvider = (processors) -> new RandomLootProvider(Arrays.asList(
                new ItemStackLootProvider(new ItemStack(PMCItems.P92), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.P1911), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.R1895), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.R45), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.P18C), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.SCORPION), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.DEAGLE), processors)
        ));
        Function<List<LootProcessor>, LootProvider> shotgunsProvider = (processors) -> new RandomLootProvider(Arrays.asList(
                new ItemStackLootProvider(new ItemStack(PMCItems.SAWED_OFF), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.S1897), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.S686), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.S12K), processors)
        ));
        Function<List<LootProcessor>, LootProvider> smgsProvider = (processors) -> new RandomLootProvider(Arrays.asList(
                new ItemStackLootProvider(new ItemStack(PMCItems.MICROUZI), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.UMP45), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.TOMMY_GUN), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.VECTOR), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.MP5K), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.BIZON), processors)
        ));
        Function<List<LootProcessor>, LootProvider> arsProvider = (processors) -> new RandomLootProvider(Arrays.asList(
                new ItemStackLootProvider(new ItemStack(PMCItems.M16A4), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.M416), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.SCAR_L), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.G36C), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.QBZ), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.AKM), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.BERYL_M762), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.MK47_MUTANT), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.DP28), processors)
        ));
        Function<List<LootProcessor>, LootProvider> dmrsProvider = (processors) -> new RandomLootProvider(Arrays.asList(
                new ItemStackLootProvider(new ItemStack(PMCItems.VSS), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.MINI14), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.QBU), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.SKS), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.SLR), processors)
        ));
        Function<List<LootProcessor>, LootProvider> srsProvider = (processors) -> new RandomLootProvider(Arrays.asList(
                new ItemStackLootProvider(new ItemStack(PMCItems.WIN94), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.KAR98K), processors),
                new ItemStackLootProvider(new ItemStack(PMCItems.M24), processors)
        ));
        LootProvider smallBackpacks = new RandomLootProvider(Arrays.asList(
                new ItemStackLootProvider(new ItemStack(PMCItems.SMALL_BACKPACK_FOREST)),
                new ItemStackLootProvider(new ItemStack(PMCItems.SMALL_BACKPACK_DESERT)),
                new ItemStackLootProvider(new ItemStack(PMCItems.SMALL_BACKPACK_SNOW))
        ));
        LootProvider mediumBackpacks = new RandomLootProvider(Arrays.asList(
                new ItemStackLootProvider(new ItemStack(PMCItems.MEDIUM_BACKPACK_FOREST)),
                new ItemStackLootProvider(new ItemStack(PMCItems.MEDIUM_BACKPACK_DESERT)),
                new ItemStackLootProvider(new ItemStack(PMCItems.MEDIUM_BACKPACK_SNOW))
        ));
        LootProvider largeBackpacks = new RandomLootProvider(Arrays.asList(
                new ItemStackLootProvider(new ItemStack(PMCItems.LARGE_BACKPACK_FOREST)),
                new ItemStackLootProvider(new ItemStack(PMCItems.LARGE_BACKPACK_DESERT)),
                new ItemStackLootProvider(new ItemStack(PMCItems.LARGE_BACKPACK_SNOW))
        ));
        LootProvider lowTierMeds = new RandomLootProvider(Arrays.asList(
                new ItemStackLootProvider(new ItemStack(PMCItems.BANDAGE, 5)),
                new ItemStackLootProvider(new ItemStack(PMCItems.ENERGYDRINK))
        ));
        LootProvider lowTierAttachments = new RandomLootProvider(Arrays.asList(
                new ItemStackLootProvider(new ItemStack(PMCItems.RED_DOT)),
                new ItemStackLootProvider(new ItemStack(PMCItems.HOLOGRAPHIC)),
                new ItemStackLootProvider(new ItemStack(PMCItems.QUICKDRAW_MAG_SMG)),
                new ItemStackLootProvider(new ItemStack(PMCItems.QUICKDRAW_MAG_AR)),
                new ItemStackLootProvider(new ItemStack(PMCItems.EXTENDED_MAG_SMG))
        ));

        // Loadouts
        LoadoutManager.register(BattleRoyaleGame.PLAYER_INITIAL_LOOT_PATH, new EntityLoadout.Builder()
                .withWeaponProvider(new ItemStackLootProvider(new ItemStack(PMCItems.PARACHUTE)))
                .build()
        );
        LoadoutManager.register(BattleRoyaleGame.AI_INITIAL_LOOT_PATH, EntityLoadout.EMPTY);
        LoadoutManager.register(BattleRoyaleGame.AI_EARLY_GAME_LOOT_PATH, new EntityLoadout.Builder()
                .withWeaponProvider(new WeightedLootProvider(Arrays.asList(
                        new WeightedRandom.Entry<>(40, pistolsProvider.apply(Collections.singletonList(pistolAmmoPack))),
                        new WeightedRandom.Entry<>(20, shotgunsProvider.apply(Collections.singletonList(shotgunAmmoPack))),
                        new WeightedRandom.Entry<>(30, smgsProvider.apply(Collections.singletonList(defaultAmmoPack))),
                        new WeightedRandom.Entry<>(10, arsProvider.apply(Collections.singletonList(defaultAmmoPack)))
                )))
                .withArmorProvider(new MultiValueLootProvider(Arrays.asList(
                        new RandomChanceLootProvider(0.3F, new ItemStackLootProvider(new ItemStack(PMCItems.ARMOR1HELMET))),
                        new RandomChanceLootProvider(0.5F, new WeightedLootProvider(Arrays.asList(
                                new WeightedRandom.Entry<>(70, new ItemStackLootProvider(new ItemStack(PMCItems.ARMOR1BODY))),
                                new WeightedRandom.Entry<>(30, new ItemStackLootProvider(new ItemStack(PMCItems.ARMOR2BODY)))
                        )))
                )))
                .withSpecialEquipmentProvider(new RandomChanceLootProvider(0.5F, new WeightedLootProvider(Arrays.asList(
                        new WeightedRandom.Entry<>(60, smallBackpacks),
                        new WeightedRandom.Entry<>(40, mediumBackpacks)
                ))))
                .withGeneralLootProvider(new CountLootProvider(0, 2, new RandomLootProvider(Arrays.asList(
                        lowTierMeds, lowTierAttachments
                ))))
                .build()
        );
    }

    private static void applyPlayerLoadout(EntityPlayerMP player, EntityLoadout loadout) {
        LootGenerationContext context = LootGenerationContext.entity(player);
        List<ItemStack> weaponItems = loadout.getWeapons(context);
        List<ItemStack> armorItems = loadout.getArmor(context);
        List<ItemStack> specialEquipment = loadout.getSpecialEquipment(context);
        List<ItemStack> generalLoot = loadout.getGeneralLoot(context);

        addToInventory(player.inventory, weaponItems, 0, 0);
        addArmor(armorItems, player);
        addToInventory(player.inventory, generalLoot, 0, 3);
        IPlayerData data = PlayerData.get(player);
        if (data != null) {
            addSpecialEquipment(data, specialEquipment);
        }
    }

    private static void applyAiLoadout(EntityAIPlayer ai, EntityLoadout loadout) {
        LootGenerationContext context = LootGenerationContext.entity(ai);
        List<ItemStack> weapon = loadout.getWeapons(context);
        List<ItemStack> armorItems = loadout.getArmor(context);
        List<ItemStack> specialEquipment = loadout.getSpecialEquipment(context);
        List<ItemStack> general = loadout.getGeneralLoot(context);

        if (!weapon.isEmpty()) {
            ai.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, weapon.get(0));
            addToInventory(ai.getInventory(), weapon.subList(1, weapon.size()), 0, 0);
        }
        addArmor(armorItems, ai);
        addSpecialEquipment(ai, specialEquipment);
        addToInventory(ai.getInventory(), general, 2, 0);

        SerializationHelper.syncEntity(ai);
    }

    private static void addToInventory(IInventory inventory, List<ItemStack> items, int index, int offset) {
        for (int i = index; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                index = i;
                break;
            }
        }
        for (ItemStack stack : items) {
            int position = index++ + offset;
            if (position >= inventory.getSizeInventory())
                break;
            inventory.setInventorySlotContents(position, stack.copy());
        }
    }

    private static void addArmor(List<ItemStack> items, EntityLivingBase entity) {
        for (ItemStack stack : items) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) stack.getItem();
                EntityEquipmentSlot slot = armor.getEquipmentSlot();
                ItemStack equipped = entity.getItemStackFromSlot(slot);
                if (equipped.isEmpty()) {
                    entity.setItemStackToSlot(slot, stack.copy());
                }
            }
        }
    }

    private static void addSpecialEquipment(SpecialInventoryProvider provider, List<ItemStack> items) {
        for (ItemStack stack : items) {
            if (!stack.isEmpty() && stack.getItem() instanceof SpecialInventoryItem) {
                SpecialInventoryItem item = (SpecialInventoryItem) stack.getItem();
                SpecialEquipmentSlot slot = item.getSlotType();
                ItemStack oldItem = provider.getSpecialItemFromSlot(slot);
                if (oldItem.isEmpty()) {
                    provider.setSpecialItemToSlot(slot, stack.copy());
                }
            }
        }
    }
}
