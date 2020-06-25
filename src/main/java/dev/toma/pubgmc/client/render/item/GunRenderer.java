package dev.toma.pubgmc.client.render.item;

import dev.toma.pubgmc.common.item.gun.GunItem;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public abstract class GunRenderer extends ItemStackTileEntityRenderer {

    public abstract GunItem getRenderItem();

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        if(itemStackIn.getItem() == this.getRenderItem()) {

        }
    }
}
