package dev.toma.pubgmc.common.container;

import dev.toma.pubgmc.common.inventory.AttachmentInventory;
import dev.toma.pubgmc.common.item.gun.GunItem;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentCategory;
import dev.toma.pubgmc.common.item.gun.attachment.AttachmentItem;
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
        for(AttachmentCategory category : AttachmentCategory.values()) {
            addSlot(new AttachmentSlot(inventory, category, stack.copy(), category.ordinal(), category.getX(), category.getY()));
        }
        for(int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(playerInventory, x + (y * 9) + 9, 8 + x * 18, 115 + y * 18));
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
