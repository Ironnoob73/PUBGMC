package toma.pubgmc.common;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import toma.pubgmc.common.item.meds.BoostItem;
import toma.pubgmc.common.item.meds.HealingItem;
import toma.pubgmc.common.item.meds.PartialHealingItem;
import toma.pubgmc.util.time.Duration;

import java.util.ArrayList;
import java.util.List;

public final class Registry {

    private static void registerBlocks(BlockRegistryHelper helper) {

    }

    private static void registerItems(RegisterEvent.RegisterHelper<Item> helper) {
        helper.register("bandage", new PartialHealingItem(2.0F, 0.75F, Duration.seconds(4), new Item.Properties().stacksTo(5)));
        helper.register("first_aid_kit", new PartialHealingItem(Float.MAX_VALUE, 0.75F, Duration.seconds(6), new Item.Properties().stacksTo(1)));
        helper.register("med_kit", new HealingItem(Float.MAX_VALUE, HealingItem.notMaxHealth(), Duration.seconds(8), new Item.Properties().stacksTo(1)));
        helper.register("energy_drink", new BoostItem(40.0F, Duration.seconds(4), new Item.Properties().stacksTo(5)));
        helper.register("painkillers", new BoostItem(60.0F, Duration.seconds(6), new Item.Properties().stacksTo(3)));
        helper.register("adrenaline_syringe", new BoostItem(100.0F, Duration.seconds(8), new Item.Properties().stacksTo(1)));
    }

    private static List<Block> blockItemEntries = new ArrayList<>();

    public static void onRegister(RegisterEvent event) {
        event.register(ForgeRegistries.BLOCKS.getRegistryKey(), helper -> {
            BlockRegistryHelper blockRegistryHelper = (name, block, registerItemBlock) -> {
                helper.register(name, block);
                if (registerItemBlock) {
                    blockItemEntries.add(block);
                }
            };
            registerBlocks(blockRegistryHelper);
        });
        event.register(ForgeRegistries.ITEMS.getRegistryKey(), helper -> {
            registerItems(helper);
            blockItemEntries.forEach(itemBlockEntry -> {
                BlockItem blockItem = new BlockItem(itemBlockEntry, new Item.Properties());
                helper.register(ForgeRegistries.BLOCKS.getKey(itemBlockEntry), blockItem);
            });
            blockItemEntries = null;
        });
    }

    @FunctionalInterface
    private interface BlockRegistryHelper {

        void register(String name, Block block, boolean registerItemBlock);

        default void register(String name, Block block) {
            register(name, block, true);
        }
    }
}
