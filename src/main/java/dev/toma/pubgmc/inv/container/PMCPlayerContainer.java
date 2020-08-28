package dev.toma.pubgmc.inv.container;

import dev.toma.pubgmc.init.PMCContainers;
import dev.toma.pubgmc.inv.cap.InventoryFactory;
import dev.toma.pubgmc.inv.cap.PMCInventoryHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.util.Optional;

public class PMCPlayerContainer extends RecipeBookContainer<CraftingInventory> {

    protected static final String[] ARMOR_SLOT_TEXTURES = {"item/empty_armor_slot_boots", "item/empty_armor_slot_leggings", "item/empty_armor_slot_chestplate", "item/empty_armor_slot_helmet"};
    private static final EquipmentSlotType[] SLOTS = {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
    public final CraftingInventory matrix = new CraftingInventory(this, 2, 2);
    public final CraftResultInventory result = new CraftResultInventory();
    private final PlayerEntity player;
    public PMCInventoryHandler invHandler;
    public boolean isLocal;

    public PMCPlayerContainer(int id, PlayerInventory inventory, PacketBuffer data) {
        this(id, inventory, inventory.player.world.isRemote, inventory.player);
    }

    public PMCPlayerContainer(int id, PlayerInventory inventory, boolean isLocal, PlayerEntity player) {
        super(PMCContainers.PLAYER_CONTAINER.get(), id);
        this.player = player;
        this.isLocal = isLocal;
        this.invHandler = InventoryFactory.getInventoryHandler(player);

        addSlot(new CraftingResultSlot(inventory.player, matrix, result, 0, 154, 28));
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                addSlot(new Slot(matrix, x + y * 2, 98 + x * 18, 18 + y * 18));
            }
        }
        for (int i = 0; i < 4; i++) {
            EquipmentSlotType type = SLOTS[i];
            addSlot(new Slot(inventory, 39 - i, 8, 8 + i * 18) {
                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack stack) {
                    return stack.canEquip(type, player);
                }

                @Override
                public boolean canTakeStack(PlayerEntity playerIn) {
                    ItemStack stack = getStack();
                    return (stack.isEmpty() || player.isCreative() || !EnchantmentHelper.hasBindingCurse(stack)) && super.canTakeStack(playerIn);
                }

                @OnlyIn(Dist.CLIENT)
                @Nullable
                @Override
                public String getSlotTexture() {
                    return ARMOR_SLOT_TEXTURES[type.getIndex()];
                }
            });
        }
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(inventory, x + (y + 1) * 9, 8 + x * 18, 84 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(inventory, x, 8 + x * 18, 142));
        }
        addSlot(new Slot(inventory, 40, 77, 62) {
            @OnlyIn(Dist.CLIENT)
            @Nullable
            @Override
            public String getSlotTexture() {
                return "item/empty_armor_slot_shield";
            }
        });
        for(int y = 0; y < 3; y++) {
            addSlot(new SlotItemHandler(invHandler, y, 77, 8 + y * 18));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public void func_201771_a(RecipeItemHelper p_201771_1_) {
        this.matrix.fillStackedContents(p_201771_1_);
    }

    public void clear() {
        this.result.clear();
        this.matrix.clear();
    }

    public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
        return recipeIn.matches(this.matrix, this.player.world);
    }

    public void onCraftMatrixChanged(IInventory inventoryIn) {
        func_217066_a(this.windowId, this.player.world, this.player, this.matrix, this.result);
    }

    protected static void func_217066_a(int p_217066_0_, World p_217066_1_, PlayerEntity p_217066_2_, CraftingInventory p_217066_3_, CraftResultInventory p_217066_4_) {
        if (!p_217066_1_.isRemote) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)p_217066_2_;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<ICraftingRecipe> optional = p_217066_1_.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, p_217066_3_, p_217066_1_);
            if (optional.isPresent()) {
                ICraftingRecipe icraftingrecipe = optional.get();
                if (p_217066_4_.canUseRecipe(p_217066_1_, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.getCraftingResult(p_217066_3_);
                }
            }
            p_217066_4_.setInventorySlotContents(0, itemstack);
            serverplayerentity.connection.sendPacket(new SSetSlotPacket(p_217066_0_, 0, itemstack));
        }
    }

    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.result.clear();
        if (!playerIn.world.isRemote) {
            this.clearContainer(playerIn, playerIn.world, this.matrix);
        }
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            EquipmentSlotType equipmentslottype = MobEntity.getSlotForItemStack(itemstack);
            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= 1 && index < 5) {
                if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 9) {
                if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslottype.getSlotType() == EquipmentSlotType.Group.ARMOR && !this.inventorySlots.get(8 - equipmentslottype.getIndex()).getHasStack()) {
                int i = 8 - equipmentslottype.getIndex();
                if (!this.mergeItemStack(itemstack1, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslottype == EquipmentSlotType.OFFHAND && !this.inventorySlots.get(45).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 9 && index < 36) {
                if (!this.mergeItemStack(itemstack1, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 36 && index < 45) {
                if (!this.mergeItemStack(itemstack1, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
            if (index == 0) {
                playerIn.dropItem(itemstack2, false);
            }
        }
        return itemstack;
    }

    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.result && super.canMergeSlot(stack, slotIn);
    }

    public int getOutputSlot() {
        return 0;
    }

    public int getWidth() {
        return this.matrix.getWidth();
    }

    public int getHeight() {
        return this.matrix.getHeight();
    }

    @OnlyIn(Dist.CLIENT)
    public int getSize() {
        return 5;
    }
}
