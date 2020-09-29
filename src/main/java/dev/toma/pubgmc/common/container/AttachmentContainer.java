package dev.toma.pubgmc.common.container;

import dev.toma.pubgmc.capability.PMCInventoryHandler;
import dev.toma.pubgmc.capability.player.InventoryFactory;
import dev.toma.pubgmc.common.inventory.AttachmentInventory;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
import dev.toma.pubgmc.common.item.gun.attachment.GunAttachments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

import static dev.toma.pubgmc.init.PMCContainers.ATTACHMENT_CONTAINER;

public class AttachmentContainer extends Container {

    private final AttachmentInventory inventory;
    private final ItemStack stack;

    public AttachmentContainer(int i, PlayerInventory inventory, PacketBuffer data) {
        this(i, inventory);
    }

    public AttachmentContainer(int i, PlayerInventory playerInventory) {
        super(ATTACHMENT_CONTAINER.get(), i);
        this.stack = playerInventory.player.getHeldItemMainhand();
        this.inventory = new AttachmentInventory(playerInventory.player);
        inventory.openInventory(playerInventory.player);
        PMCInventoryHandler handler = InventoryFactory.getInventoryHandler(playerInventory.player);
        for(AttachmentCategory category : AttachmentCategory.values()) {
            addSlot(new AttachmentSlot(inventory, category, stack.copy(), category.ordinal(), category.getX(), category.getY()));
        }
        for(int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new BackpackLockableSlot(playerInventory, x + (y * 9) + 9, 8 + x * 18, 115 + y * 18, handler));
            }
        }
        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(playerInventory, x, 8 + x * 18, 173));
        }
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        inventory.closeInventory(playerIn);
        super.onContainerClosed(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemStack;
        Slot slot = inventorySlots.get(index);
        if(slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if(index <= 4) {
                if(!this.mergeItemStack(itemStack1, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemStack, itemStack1);
            } else {
                boolean handleInventoryTransfer = true;
                if(itemStack1.getItem() instanceof AttachmentItem) {
                    AttachmentItem item = (AttachmentItem) itemStack1.getItem();
                    int id = item.getCategory().ordinal();
                    if(!(stack.getItem() instanceof GunItem)) {
                        return ItemStack.EMPTY;
                    }
                    GunAttachments attachments = ((GunItem) stack.getItem()).getAttachmentList();
                    if(attachments.canAttach(item)) {
                        handleInventoryTransfer = false;
                        Slot target = inventorySlots.get(id);
                        if(target != null) {
                            ItemStack copy = null;
                            if(target.getHasStack()) {
                                copy = target.getStack();
                            }
                            target.putStack(itemStack1.copy());
                            slot.getStack().setCount(0);
                            if(copy != null) slot.putStack(copy);
                            return ItemStack.EMPTY;
                        }
                    }
                }
                if(handleInventoryTransfer) {
                    if(index > 4 && index < 32) {
                        if(!mergeItemStack(itemStack1, 32, 41, false)) {
                            return ItemStack.EMPTY;
                        }
                        slot.onSlotChange(itemStack1, itemStack);
                    } else if(index >= 32) {
                        if(!mergeItemStack(itemStack1, 5, 32, false)) {
                            return ItemStack.EMPTY;
                        }
                        slot.onSlotChange(itemStack1, itemStack);
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    static class AttachmentSlot extends Slot {

        private final ItemStack stack;
        private final AttachmentCategory category;
        private final String texture;

        public AttachmentSlot(AttachmentInventory inventory, AttachmentCategory category, ItemStack stack, int i, int x, int y) {
            super(inventory, i, x, y);
            this.stack = stack;
            this.category = category;
            this.texture = category.getSlotTexture();
        }

        boolean isCategorySupported() {
            return ((GunItem) this.stack.getItem()).getAttachmentList().supports(category);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            if(stack.getItem() instanceof AttachmentItem) {
                return ((GunItem) this.stack.getItem()).getAttachmentList().canAttach((AttachmentItem) stack.getItem());
            }
            return false;
        }

        @Nullable
        @Override
        public String getSlotTexture() {
            return isCategorySupported() ? texture : "pubgmc:slot/locked";
        }
    }
}
