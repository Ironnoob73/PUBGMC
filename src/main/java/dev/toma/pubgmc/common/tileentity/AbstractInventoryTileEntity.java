package dev.toma.pubgmc.common.tileentity;

import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public abstract class AbstractInventoryTileEntity extends TileEntity implements INamedContainerProvider {

    protected LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createInventory);

    public AbstractInventoryTileEntity(TileEntityType<?> type) {
        super(type);
    }

    public abstract IItemHandler createInventory();

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), -1, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        inventory.ifPresent(inv -> {
            CompoundNBT nbt = ((INBTSerializable<CompoundNBT>) inv).serializeNBT();
            compound.put("inventory", nbt);
        });
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        inventory.ifPresent(inv -> ((INBTSerializable<CompoundNBT>) inv).deserializeNBT(compound.getCompound("inventory")));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventory.cast();
        }
        return super.getCapability(cap, side);
    }

    public LazyOptional<IItemHandler> getInventory() {
        return inventory;
    }

    public void clearInventory() {
        inventory.ifPresent(inv -> {
            for(int i = 0; i < inv.getSlots(); i++) {
                inv.extractItem(i, inv.getSlotLimit(i), false);
            }
        });
    }
}
