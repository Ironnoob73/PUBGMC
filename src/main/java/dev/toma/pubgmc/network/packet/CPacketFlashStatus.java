package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.common.entity.throwable.FlashEntity;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CPacketFlashStatus implements NetworkPacket<CPacketFlashStatus> {

    public CPacketFlashStatus() {
    }

    private boolean blind;

    public CPacketFlashStatus(boolean blind) {
        this.blind = blind;
    }

    @Override
    public void encode(CPacketFlashStatus instance, PacketBuffer buf) {
        buf.writeBoolean(instance.blind);
    }

    @Override
    public CPacketFlashStatus decode(PacketBuffer buf) {
        return new CPacketFlashStatus(buf.readBoolean());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(CPacketFlashStatus instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> FlashEntity.FlashHandler.ClientHandler.update(instance.blind));
        ctx.get().setPacketHandled(true);
    }
}
