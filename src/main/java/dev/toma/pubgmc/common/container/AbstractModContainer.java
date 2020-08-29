package dev.toma.pubgmc.common.container;

import dev.toma.pubgmc.capability.InventoryFactory;
import dev.toma.pubgmc.capability.PMCInventoryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

@SuppressWarnings("unchecked")
public abstract class AbstractModContainer<T extends TileEntity> extends Container {

    protected final T tileEntity;
    private int invSize;

    public AbstractModContainer(ContainerType<?> type, int windowID, PlayerInventory inventory, T tileEntity) {
        super(type, windowID);
        this.tileEntity = tileEntity;
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            invSize = h.getSlots();
        });
    }

    public AbstractModContainer(ContainerType<?> type, int windowID, PlayerInventory inventory, PacketBuffer buffer) {
        this(type, windowID, inventory, getTile(inventory.player.world, buffer));
    }

    protected static <T extends TileEntity> T getTile(World world, PacketBuffer buffer) {
        return (T) world.getTileEntity(buffer.readBlockPos());
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(playerIn.world, tileEntity.getPos()), playerIn, playerIn.world.getBlockState(tileEntity.getPos()).getBlock());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < invSize) {
                if (!this.mergeItemStack(itemstack1, invSize, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, invSize, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    protected void addPlayerInventory(PlayerInventory inv, int yStart) {
        PMCInventoryHandler handler = InventoryFactory.getInventoryHandler(inv.player);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new BackpackLockableSlot(inv, 9 + x + y * 9, 8 + x * 18, yStart + y * 18, handler));
            }
        }
        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(inv, x, 8 + x * 18, yStart + 58));
        }
    }
}
