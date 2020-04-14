package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CPacketSendNBT implements NetworkPacket<CPacketSendNBT> {

    public static int PLAYER_CAP_SYNC_NETWORK = 0;
    public static int PLAYER_CAP_SYNC_FULL = 1;

    private CompoundNBT nbt;
    private int id;

    public CPacketSendNBT(CompoundNBT nbt, int id) {
        this.nbt = nbt;
        this.id = id;
    }

    @Override
    public void encode(CPacketSendNBT instance, PacketBuffer buf) {
        buf.writeCompoundTag(instance.nbt);
        buf.writeInt(instance.id);
    }

    @Override
    public CPacketSendNBT decode(PacketBuffer buf) {
        return new CPacketSendNBT(buf.readCompoundTag(), buf.readInt());
    }

    @Override
    public void handle(CPacketSendNBT instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Type.values()[instance.id].playerEntityConsumer.accept(mc.player, instance);
        });
        ctx.get().setPacketHandled(true);
    }

    private enum Type {
        PLAYER_CAP((player, packet) -> PlayerCapFactory.get(player).loadNetworkData(packet.nbt)),
        PLAYER_CAP_COMPLETE((player, packet) -> PlayerCapFactory.get(player).deserializeNBT(packet.nbt));

        private final BiConsumer<PlayerEntity, CPacketSendNBT> playerEntityConsumer;

        Type(BiConsumer<PlayerEntity, CPacketSendNBT> playerEntityConsumer) {
            this.playerEntityConsumer = playerEntityConsumer;
        }
    }
}
