package dev.toma.pubgmc.common.block;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.Registry;
import dev.toma.pubgmc.common.item.PMCItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import javax.annotation.Nullable;

public class PMCBlock extends Block {

    public PMCBlock(String name, Properties properties) {
        super(properties);
        this.setRegistryName(Pubgmc.makeResource(name));
        Registry.CommonHandler.blockItemList.add(this.makeItem());
    }

    @Nullable
    public BlockItem makeItem() {
        BlockItem blockItem = new BlockItem(this, new Item.Properties().group(PMCItem.ITEMS));
        blockItem.setRegistryName(this.getRegistryName());
        return blockItem;
    }
}
