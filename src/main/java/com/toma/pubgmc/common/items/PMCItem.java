package com.toma.pubgmc.common.items;

import com.toma.pubgmc.PMCTabs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PMCItem extends Item {
    public List<String> desc = new ArrayList<String>();

    public PMCItem(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(PMCTabs.TAB_ITEMS);
    }

    public Item addDescription(String... strings) {
        for (String s : strings) {
            desc.add(s);
        }

        return this;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!desc.isEmpty()) {
            for (int i = 0; i < desc.size(); i++) {
                tooltip.add(desc.get(i));
            }
        }
    }

    public void warnPlayer(EntityPlayer player, String warning) {
        if (player != null && !player.world.isRemote) {
            player.sendMessage(new TextComponentString(TextFormatting.RED + warning));
        }
    }
}
