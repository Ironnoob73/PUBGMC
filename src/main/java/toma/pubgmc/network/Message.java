package toma.pubgmc.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Message {

    public abstract void encode(FriendlyByteBuf buffer);

    public abstract void process(NetworkEvent.Context context);

    public final void handleMessage(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> this.process(context));
        context.setPacketHandled(true);
    }

    @FunctionalInterface
    public interface Decoder<M> extends Function<FriendlyByteBuf, M> {}
}
