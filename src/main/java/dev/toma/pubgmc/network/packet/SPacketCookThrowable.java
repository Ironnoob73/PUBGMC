package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.common.item.utility.ThrowableItem;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketCookThrowable implements NetworkPacket<SPacketCookThrowable> {

    @Override
    public void encode(SPacketCookThrowable instance, PacketBuffer buf) {

    }

    @Override
    public SPacketCookThrowable decode(PacketBuffer buf) {
        return new SPacketCookThrowable();
    }

    @Override
    public void handle(SPacketCookThrowable instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            ItemStack stack = player.getHeldItemMainhand();
            if(!stack.isEmpty() && stack.getItem() instanceof ThrowableItem) {
                ((ThrowableItem) stack.getItem()).startCooking(stack);
            }
            player.world.playSound(null,  player.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 1.0F, 1.0F);
        });
        ctx.get().setPacketHandled(true);
    }
}
