package toma.pubgmc.common;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.List;

public final class Registry {

    private static void registerBlocks(BlockRegistryHelper helper) {

    }

    private static void registerItems(RegisterEvent.RegisterHelper<Item> helper) {

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
