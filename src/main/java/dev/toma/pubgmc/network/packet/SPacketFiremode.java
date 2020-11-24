package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketFiremode implements NetworkPacket<SPacketFiremode> {

    @Override
    public void encode(SPacketFiremode instance, PacketBuffer buf) {

    }

    @Override
    public SPacketFiremode decode(PacketBuffer buf) {
        return new SPacketFiremode();
    }

    @Override
    public void handle(SPacketFiremode instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof AbstractGunItem) {
                ((AbstractGunItem) stack.getItem()).switchFiremode(player, stack);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
