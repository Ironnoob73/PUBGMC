package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.capability.IPlayerCap;
import dev.toma.pubgmc.capability.player.PlayerCapFactory;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CPacketSendNBT implements NetworkPacket<CPacketSendNBT> {

    public static int PLAYER_CAP_SYNC_NETWORK = 0;
    public static int PLAYER_CAP_SYNC_FULL = 1;

    private final UUID uuid;
    private final CompoundNBT nbt;
    private final int id;

    public CPacketSendNBT(UUID uuid, CompoundNBT nbt, int id) {
        this.uuid = uuid;
        this.nbt = nbt;
        this.id = id;
    }

    @Override
    public void encode(CPacketSendNBT instance, PacketBuffer buf) {
        buf.writeUniqueId(instance.uuid);
        buf.writeCompoundTag(instance.nbt);
        buf.writeInt(instance.id);
    }

    @Override
    public CPacketSendNBT decode(PacketBuffer buf) {
        return new CPacketSendNBT(buf.readUniqueId(), buf.readCompoundTag(), buf.readInt());
    }

    @Override
    public void handle(CPacketSendNBT instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.world.getPlayerByUuid(instance.uuid);
            if(player != null) {
                Type.values()[instance.id].run(player, instance);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private enum Type {
        PLAYER_CAP((player, packet) -> {
            IPlayerCap cap = PlayerCapFactory.get(player);
            cap.loadNetworkData(packet.nbt);
            if(cap.isProne()) {
                AxisAlignedBB proneBB = new AxisAlignedBB(player.posX - 0.6, player.posY, player.posZ - 0.6, player.posX + 0.6, player.posY + 0.8, player.posZ + 0.6);
                player.setBoundingBox(proneBB);
                player.eyeHeight = 0.6F;
                player.size = new EntitySize(1.25F, 0.8F, false);
            }
        }),
        PLAYER_CAP_COMPLETE((player, packet) -> PlayerCapFactory.get(player).deserializeNBT(packet.nbt));

        private final BiConsumer<PlayerEntity, CPacketSendNBT> playerEntityConsumer;

        Type(BiConsumer<PlayerEntity, CPacketSendNBT> playerEntityConsumer) {
            this.playerEntityConsumer = playerEntityConsumer;
        }

        public void run(PlayerEntity player, CPacketSendNBT packet) {
            playerEntityConsumer.accept(player, packet);
        }
    }
}
