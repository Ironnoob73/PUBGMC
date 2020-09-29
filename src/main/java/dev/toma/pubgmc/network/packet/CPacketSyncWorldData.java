package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.capability.world.WorldDataProvider;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketSyncWorldData implements NetworkPacket<CPacketSyncWorldData> {

    private CompoundNBT nbt;

    public CPacketSyncWorldData() {}

    public CPacketSyncWorldData(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    @Override
    public void encode(CPacketSyncWorldData instance, PacketBuffer buf) {
        buf.writeCompoundTag(instance.nbt);
    }

    @Override
    public CPacketSyncWorldData decode(PacketBuffer buf) {
        return new CPacketSyncWorldData(buf.readCompoundTag());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(CPacketSyncWorldData instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;
            if(instance.nbt != null) {
                world.getCapability(WorldDataProvider.CAP).ifPresent(cap -> cap.deserializeNBT(instance.nbt));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
