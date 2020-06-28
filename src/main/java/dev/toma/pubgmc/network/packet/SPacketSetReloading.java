package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.capability.player.ReloadInfo;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketSetReloading implements NetworkPacket<SPacketSetReloading> {

    boolean reloading;
    int time;

    public SPacketSetReloading() {

    }

    public SPacketSetReloading(boolean reloading, int time) {
        this.reloading = reloading;
        this.time = time;
    }

    @Override
    public void encode(SPacketSetReloading instance, PacketBuffer buf) {
        buf.writeBoolean(instance.reloading);
        buf.writeInt(instance.time);
    }

    @Override
    public SPacketSetReloading decode(PacketBuffer buf) {
        return new SPacketSetReloading(buf.readBoolean(), buf.readInt());
    }

    @Override
    public void handle(SPacketSetReloading instance, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            IPlayerCap cap = PlayerCapFactory.get(player);
            ReloadInfo reloadInfo = cap.getReloadInfo();
            if(instance.reloading) {
                reloadInfo.startReloading(player.inventory.currentItem, instance.time);
            } else reloadInfo.cancelReload();
            cap.syncNetworkData();
        });
        context.setPacketHandled(true);
    }
}
