package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketSyncEntity implements NetworkPacket<CPacketSyncEntity> {

    public CPacketSyncEntity() {

    }

    private int id;
    private CompoundNBT nbt;

    public CPacketSyncEntity(int id, CompoundNBT nbt) {
        this.id = id;
        this.nbt = nbt;
    }

    @Override
    public void encode(CPacketSyncEntity instance, PacketBuffer buf) {
        buf.writeInt(instance.id);
        buf.writeCompoundTag(instance.nbt);
    }

    @Override
    public CPacketSyncEntity decode(PacketBuffer buf) {
        return new CPacketSyncEntity(buf.readInt(), buf.readCompoundTag());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(CPacketSyncEntity instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;
            Entity entity = world.getEntityByID(instance.id);
            if(entity == null) {
                Pubgmc.pubgmcLog.warn("Received sync packet for unknown entity");
                return;
            }
            entity.read(instance.nbt);
        });
        ctx.get().setPacketHandled(true);
    }
}
