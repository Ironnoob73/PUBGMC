package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.capability.player.PlayerCapProvider;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketProne implements NetworkPacket<SPacketProne> {

    private boolean prone;

    public SPacketProne() {}

    public SPacketProne(boolean prone) {
        this.prone = prone;
    }

    @Override
    public void encode(SPacketProne instance, PacketBuffer buf) {
        buf.writeBoolean(instance.prone);
    }

    @Override
    public SPacketProne decode(PacketBuffer buf) {
        return new SPacketProne(buf.readBoolean());
    }

    @Override
    public void handle(SPacketProne instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            player.getCapability(PlayerCapProvider.CAP).ifPresent(cap -> {
                cap.setProne(instance.prone);
                if(!instance.prone) {
                    // forces reset
                    player.setSneaking(true);
                }
                cap.syncNetworkData();
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
