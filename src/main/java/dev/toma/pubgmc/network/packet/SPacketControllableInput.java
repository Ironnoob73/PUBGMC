package dev.toma.pubgmc.network.packet;

import dev.toma.pubgmc.common.entity.IControllableEntity;
import dev.toma.pubgmc.network.NetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketControllableInput implements NetworkPacket<SPacketControllableInput> {

    public SPacketControllableInput() {
    }

    private boolean f, b, r, l, u, d;

    public SPacketControllableInput(boolean f, boolean b, boolean r, boolean l, boolean u, boolean d) {
        this.f = f;
        this.b = b;
        this.r = r;
        this.l = l;
        this.u = u;
        this.d = d;
    }

    @Override
    public void encode(SPacketControllableInput instance, PacketBuffer buf) {
        buf.writeBoolean(instance.f);
        buf.writeBoolean(instance.b);
        buf.writeBoolean(instance.r);
        buf.writeBoolean(instance.l);
        buf.writeBoolean(instance.u);
        buf.writeBoolean(instance.d);
    }

    @Override
    public SPacketControllableInput decode(PacketBuffer buf) {
        return new SPacketControllableInput(buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    @Override
    public void handle(SPacketControllableInput instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if(player.getRidingEntity() instanceof IControllableEntity) {
                ((IControllableEntity) player.getRidingEntity()).onInputUpdate(instance.f, instance.b, instance.r, instance.l, instance.u, instance.d);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
