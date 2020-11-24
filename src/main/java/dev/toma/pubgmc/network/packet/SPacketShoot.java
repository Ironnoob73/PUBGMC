package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.common.item.gun.core.AbstractGunItem;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketShoot implements NetworkPacket<SPacketShoot> {

    @Override
    public void encode(SPacketShoot instance, PacketBuffer buf) {

    }

    @Override
    public SPacketShoot decode(PacketBuffer buf) {
        return new SPacketShoot();
    }

    @Override
    public void handle(SPacketShoot instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof AbstractGunItem) {
                ((AbstractGunItem) stack.getItem()).shoot(player, player.world, stack);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
