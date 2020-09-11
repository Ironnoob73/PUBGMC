package dev.toma.pubgmc.common.inventory;

import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
import dev.toma.pubgmc.common.item.gun.attachment.GunAttachments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class AttachmentInventory extends Inventory {

    private final PlayerEntity player;

    public AttachmentInventory(PlayerEntity player) {
        super(5);
        this.player = player;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        ItemStack stack = player.getHeldItemMainhand();
        if(!validate(stack, player)) {
            return;
        }
        CompoundNBT nbt = ((GunItem) stack.getItem()).getOrCreateTag(stack).getCompound("attachments");
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack itemStack = detach(nbt, i);
            if(!itemStack.isEmpty()) {
                setInventorySlotContents(i, itemStack);
            }
        }
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        ItemStack stack = player.getHeldItemMainhand();
        if(!validate(stack, player)) {
            return;
        }
        GunAttachments attachments = ((GunItem) stack.getItem()).getAttachmentList();
        CompoundNBT nbt = ((GunItem) stack.getItem()).getOrCreateTag(stack).getCompound("attachments");
        for(int i = 0; i < getSizeInventory(); i++) {
            ItemStack itemStack = getStackInSlot(i);
            if(!itemStack.isEmpty()) {
                attach(nbt, itemStack, attachments);
            }
        }
    }

    public void attach(CompoundNBT nbt, ItemStack stack, GunAttachments attachments) {
        if(stack.getItem() instanceof AttachmentItem) {
            AttachmentItem item = (AttachmentItem) stack.getItem();
            AttachmentCategory category = item.getCategory();
            String key = category.ordinal() + "";
            if(!attachments.canAttach(item)) {
                dropItem(stack);
                return;
            }
            nbt.putString(key, item.getRegistryName().toString());
        } else {
            dropItem(stack);
        }
    }

    public ItemStack detach(CompoundNBT nbt, int id) {
        String key = id + "";
        if(nbt.contains(key)) {
            ResourceLocation location = new ResourceLocation(nbt.getString(key));
            Item it = ForgeRegistries.ITEMS.getValue(location);
            if(it instanceof AttachmentItem) {
                nbt.remove(key);
                return new ItemStack(it);
            }
        }
        return ItemStack.EMPTY;
    }

    void dropItem(ItemStack stack) {
        if(!player.world.isRemote) {
            ItemEntity entity = new ItemEntity(player.world, player.posX, player.posY, player.posZ, stack.copy());
            stack.setCount(0);
            entity.setPickupDelay(40);
            player.world.addEntity(entity);
        }
    }

    boolean validate(ItemStack stack, PlayerEntity player) {
        if(!(stack.getItem() instanceof GunItem)) {
            for (int i = 0; i < getSizeInventory(); i++) {
                ItemStack itemStack = getStackInSlot(i);
                if(!itemStack.isEmpty()) {
                    dropItem(itemStack);
                }
            }
            return false;
        }
        return true;
    }
}
