package dev.toma.pubgmc.common.item;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.Registry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class PMCItem extends Item {

    public static final ItemGroup ITEMS = new ItemGroup("pubgmc.item_tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registry.PMCItems.SPAWN_GLIDER);
        }
    };
    public static final ItemGroup UTILITY = new ItemGroup("pubgmc.utility_tab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registry.PMCItems.CROSSBOW_BOLT);
        }
    };
    public static final ItemGroup GUNS = new ItemGroup("pubgmc.gun_tab") {
        @Override
        public ItemStack createIcon() {
            return ItemStack.EMPTY;
        }
    };

    public PMCItem(String name, Properties properties) {
        super(properties);
        this.setRegistryName(Pubgmc.makeResource(name));
    }
}
