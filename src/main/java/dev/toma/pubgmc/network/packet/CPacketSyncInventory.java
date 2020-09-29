package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.capability.PMCInventoryHandler;
import dev.toma.pubgmc.capability.player.InventoryFactory;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketSyncInventory implements NetworkPacket<CPacketSyncInventory> {

    private int id;
    private byte slot;
    private ItemStack stack;

    public CPacketSyncInventory() {
    }

    public CPacketSyncInventory(int playerId, int slot, ItemStack stack) {
        this.id = playerId;
        this.slot = (byte) slot;
        this.stack = stack;
    }

    @Override
    public void encode(CPacketSyncInventory instance, PacketBuffer buf) {
        buf.writeInt(instance.id);
        buf.writeByte(instance.slot);
        buf.writeItemStack(instance.stack);
    }

    @Override
    public CPacketSyncInventory decode(PacketBuffer buf) {
        return new CPacketSyncInventory(buf.readInt(), buf.readByte(), buf.readItemStack());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(CPacketSyncInventory instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            if(world == null) return;
            Entity received = world.getEntityByID(instance.id);
            if(received instanceof PlayerEntity) {
                PMCInventoryHandler handler = InventoryFactory.getInventoryHandler((PlayerEntity) received);
                handler.setStackInSlot(instance.slot, instance.stack);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
