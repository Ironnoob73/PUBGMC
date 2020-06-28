package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketSetAiming implements NetworkPacket<SPacketSetAiming> {

    boolean aiming;
    float speed;

    public SPacketSetAiming() {

    }

    public SPacketSetAiming(boolean aiming) {
        this(aiming, 0.25F);
    }

    public SPacketSetAiming(boolean aiming, float speed) {
        this.aiming = aiming;
        this.speed = speed;
    }

    @Override
    public void encode(SPacketSetAiming instance, PacketBuffer buf) {
        buf.writeBoolean(instance.aiming);
        buf.writeFloat(instance.speed);
    }

    @Override
    public SPacketSetAiming decode(PacketBuffer buf) {
        return new SPacketSetAiming(buf.readBoolean(), buf.readFloat());
    }

    @Override
    public void handle(SPacketSetAiming instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            IPlayerCap cap = PlayerCapFactory.get(player);
            cap.getAimInfo().setAiming(instance.aiming, instance.speed);
            cap.syncNetworkData();
        });
        ctx.get().setPacketHandled(true);
    }
}
