package dev.toma.pubgmc.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface NetworkPacket<T> {

    void encode(T instance, PacketBuffer buf);

    T decode(PacketBuffer buf);

    void handle(T instance, Supplier<NetworkEvent.Context> ctx);
}
