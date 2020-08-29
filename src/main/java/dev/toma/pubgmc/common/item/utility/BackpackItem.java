package dev.toma.pubgmc.common.item.utility;

import dev.toma.pubgmc.capability.InventoryFactory;
import dev.toma.pubgmc.capability.PMCInventoryHandler;
import dev.toma.pubgmc.common.inventory.PMCInventoryItem;
import dev.toma.pubgmc.common.item.PMCItem;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BackpackItem extends PMCItem implements BackpackSlotItem {

    private final BackpackType type;

    public BackpackItem(String name, BackpackType type) {
        super(name, new Properties().maxStackSize(1).group(ITEMS));
        this.type = type;
    }

    @Override
    public BackpackType getType() {
        return type;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        PMCInventoryHandler handler = InventoryFactory.getInventoryHandler(player);
        ItemStack held = player.getHeldItem(hand);
        ItemStack backpackSlot = handler.getStackInSlot(2);
        if(backpackSlot.isEmpty()) {
            PlayerInventory inventory = player.inventory;
            ItemStack stack = held.copy();
            held.shrink(1);
            handler.insertItem(2, stack, false);
        } else if(backpackSlot.getItem() instanceof BackpackSlotItem) {
            int equipped = ((BackpackSlotItem) backpackSlot.getItem()).getType().ordinal();
            int c = type.ordinal();
            if (c > equipped) {
                ItemStack fromSlot = backpackSlot.copy();
                ItemStack equip = held.copy();
                backpackSlot.shrink(1);
                if(!world.isRemote) {
                    world.addEntity(new ItemEntity(world, player.posX, player.posY, player.posZ, fromSlot));
                }
                handler.insertItem(2, equip, false);
                held.shrink(1);
            }
        }
        return super.onItemRightClick(world, player, hand);
    }
}
